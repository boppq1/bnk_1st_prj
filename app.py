# app.py
import os
from fastapi import FastAPI
from pydantic import BaseModel
from dotenv import load_dotenv
from langchain_google_genai import GoogleGenerativeAIEmbeddings, ChatGoogleGenerativeAI
from langchain_chroma import Chroma
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnableLambda, RunnablePassthrough

load_dotenv()
os.environ["GOOGLE_API_KEY"] = os.getenv("GOOGLE_API_KEY")

app = FastAPI(title="Busan Bank RAG Server")

# 1. 이미 저장된 Chroma DB 불러오기 (PDF 로드X, 쪼개기X -> 초고속 로드)
save_directory = "./chroma_docs_db"
google_embedding_model = GoogleGenerativeAIEmbeddings(model="models/gemini-embedding-001")

# persist_directory만 지정하면 저장된 데이터를 그대로 읽어옵니다.
db = Chroma(persist_directory=save_directory, embedding_function=google_embedding_model)
retriever = db.as_retriever(
search_type="mmr",
search_kwargs={"k": 6, "fetch_k": 15}
)

# 2. 영문 버전 부산은행 프롬프트 설정
prompt = ChatPromptTemplate.from_messages([
    (
        "system",
        """
You are the dedicated AI Financial Assistant for BNK Busan Bank's smart financial services. 
Your mission is to provide accurate, reliable, and helpful answers to the user's questions based on the provided [#Financial Context].

[Response Guidelines]
1. Tone & Manner: Always respond in Korean, maintaining a polite, professional, and trustworthy tone appropriate for a BNK Busan Bank assistant (using polite Korean honorifics like "~입니다", "~합니다").
2. Context Matching: Understand the user's intent even if their wording is slightly different from the context. If the context contains relevant solutions or guidelines (e.g., telephone numbers, action items), synthesize them naturally to answer the user's inquiry.
3. Handling Unknowns: If the provided context completely lacks any relevant information about the question, do not hallucinate. Instead, respond with this exact courteous message in Korean:
   "죄송합니다, 고객님. 해당 질문에 대한 정확한 정보를 확인하기 어렵습니다. 자세한 내용은 부산은행 영업점 또는 고객센터로 문의해 주시면 친절히 안내해 드리겠습니다."
4. Clarity: If the context contains complex financial jargon, explain it kindly and clearly so that the bank's customers can easily understand it.

5. Formatting & Readability (Strict Layout Rules):
   - CRITICAL: You must use double line breaks (\\n\\n) to separate different paragraphs and bullet points. Never bunch sentences together into a single dense paragraph.
   - Clean Markdown: Do NOT duplicate markdown stars (e.g., avoid '***'). Use exactly '- Title: Description' format.
   - For listing product details (eligibility, amount, etc.), you MUST start each item on a brand new line. Do not write them continuously.

[Output Format Example]
고객님, 미성년자 고객님을 위한 금융 상품으로 '꿈이룸 외화자유적금'이 있습니다. <br>

이 상품은 만 18세 이하 고객님을 위한 자유적립식 외화 적금 상품으로, 주요 특징은 다음과 같습니다. <br><br>

- 상품 종류: 자유적립식 외화 적금 상품<br>
- 가입 대상: 만 18세 이하의 국민인 거주자 (1인 1계좌)<br>
- 가입 방법: 부산은행 영업점 방문<br>
<br>
이 상품에 대해 더 궁금하신 점이 있으시면 언제든지 문의해 주십시오.

#Financial Context:
{context}
""",
    ),
    ("human", "{question}"),
])

# 3. LLM 설정 (FastAPI 내부 통신을 위해 streaming은 끕니다)
llm = ChatGoogleGenerativeAI(model="gemini-2.5-flash", temperature=0)

def format_docs(docs):
    return "\n\n".join(document.page_content for document in docs)

# 4. RAG 체인 조립
chain = {
    "context": retriever | RunnableLambda(format_docs),
    "question": RunnablePassthrough()
} | prompt | llm | StrOutputParser()


# 5. Spring Boot와 통신할 API 규격 정의 (DTO)
class QuestionRequest(BaseModel):
    question: str

class AnswerResponse(BaseModel):
    answer: str

# app.py 의 맨 아래 6번 섹션을 이 디버깅 코드로 덮어쓰기 해보세요.

@app.post("/ai/ask", response_model=AnswerResponse)
async def ask_question(payload: QuestionRequest):
    print(f"\n[디버깅] 사용자의 실제 질문: {payload.question}")
    
    # 1. DB에서 실제로 검색된 문서들이 뭔지 강제로 리스트로 가져옵니다.
    retrieved_docs = retriever.invoke(payload.question)
    
    print(f"[디버깅] DB에서 찾아온 문서 개수: {len(retrieved_docs)}개")
    print("--------------------------------------------------")
    for idx, doc in enumerate(retrieved_docs):
        print(f"👉 찾아온 문서 [{idx+1}]:\n{doc.page_content[:200]}...") # 앞 200자만 출력
        print("--------------------------------------------------")
        
    # 2. 원래대로 제미나이 체인 실행
    ai_answer = chain.invoke(payload.question)
    return AnswerResponse(answer=ai_answer)

# 서버 실행용 코드
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)