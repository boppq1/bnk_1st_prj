# init_db.py
import os
import shutil
import time
from dotenv import load_dotenv
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_google_genai import GoogleGenerativeAIEmbeddings
from langchain_chroma import Chroma

load_dotenv()
os.environ["GOOGLE_API_KEY"] = os.getenv("GOOGLE_API_KEY")

save_directory = "./chroma_docs_db"
if os.path.exists(save_directory):
    shutil.rmtree(save_directory)

print("1. PDF 문서 로드 중...")
loader = PyPDFLoader("외환.pdf")
docs = loader.load()

print("2. 텍스트 분할 중 (공백 뭉침 대응 버전)...")
# 띄어쓰기가 붙어 나오는 현상 해결 위해 분할 전략 수정
text_splitter = RecursiveCharacterTextSplitter(
    separators=['\n\n', '\n', 'Q.', 'A.', ' '], # 질문과 답변 경계를 기준으로 우선 분할
    chunk_size=400,         # 뭉쳐진 글자 특성을 고려해 사이즈를 400자로 줄여 밀도를 높임
    chunk_overlap=200,      # 200자나 겹치게 하여 문장이 잘려도 무조건 검색되게 함
    length_function=len     # 순수 글자 수 세기 적용
)


text_splitter = RecursiveCharacterTextSplitter(
    separators=['\n\n', '\n', 'Q.', 'A.', ' '],
    chunk_size=400,     
    chunk_overlap=200,   
    length_function=len   
)


texts = text_splitter.split_documents(docs)
print(f"총 {len(texts)}개의 금융 청크로 분할되었습니다.")

print("3. 구글 모델로 벡터화 및 디스크에 영구 저장 중...")
google_embedding_model = GoogleGenerativeAIEmbeddings(model="models/gemini-embedding-001")

db = Chroma(persist_directory=save_directory, embedding_function=google_embedding_model)

for i, text in enumerate(texts):
    print(f"[{i+1}/{len(texts)}] 번째 문서를 벡터 DB에 굽는 중...")
    db.add_documents([text])
    time.sleep(1.5)

print("🎉 빌드 완료! ./chroma_docs_db 폴더에 데이터가 안전하게 저장되었습니다.")