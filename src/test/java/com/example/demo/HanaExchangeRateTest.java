package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HanaExchangeRateTest {

	// ============================================================
	// ✅ 발급받은 값으로 교체하세요
	// ============================================================
	private static final String AUTH_TOKEN = "여기에_B2B인증토큰";
	// 예) COMPAN192168105010D0001|20171018163524|4TJ5GBlKOvcMq+Np/...

	private static final String ENTR_CD = "여기에_업체코드"; // 예) COMPAN
	private static final String CLNT_IP = "여기에_내_IP주소"; // 예) 123.123.123.123
	// ============================================================

	private static final String API_URL = "https://openapi.kebhana.com/kebhnb/ldm/v1/inquiry/exchangeRate";

	// 1~58번 통화코드 목록 (명세서 기준 정확한 순서)
	private static final String[][] CURRENCIES = { { "USD", "달러", "미국" }, { "JPY", "100엔", "일본" },
			{ "EUR", "유로", "유로" }, { "CNY", "위안", "중국" }, { "HKD", "달러", "홍콩" }, { "THB", "바트", "태국" },
			{ "TWD", "달러", "대만" }, { "PHP", "페소", "필리핀" }, { "SGD", "달러", "싱가포르" }, { "AUD", "달러", "호주" },
			{ "VND", "100동", "베트남" }, { "GBP", "파운드", "영국" }, { "CAD", "달러", "캐나다" }, { "MYR", "링기트", "말레이지아" },
			{ "RUB", "루블", "러시아" }, { "ZAR", "랜드", "남아공화국" }, { "NOK", "크로나", "노르웨이" }, { "NZD", "달러", "뉴질랜드" },
			{ "DKK", "크로나", "덴마크" }, { "MXN", "페소", "멕시코" }, { "MNT", "투그릭", "몽골" }, { "BHD", "디나르", "바레인" },
			{ "BDT", "타카", "방글라데시" }, { "BRL", "헤알", "브라질" }, { "BND", "달러", "브루나이" }, { "SAR", "리얄", "사우디아라비아" },
			{ "LKR", "루피", "스리랑카" }, { "SEK", "크로나", "스웨덴" }, { "CHF", "프랑", "스위스" }, { "AED", "디르함", "아랍에미리트공화국" },
			{ "DZD", "알제리 디나르", "알제리" }, { "OMR", "리얄", "오만" }, { "JOD", "디나르", "요르단" }, { "ILS", "셰켈", "이스라엘" },
			{ "EGP", "파운드", "이집트" }, { "INR", "루피", "인도" }, { "IDR", "100루피아", "인도네시아" }, { "CZK", "코루나", "체코" },
			{ "CLP", "페소", "칠레" }, { "KZT", "텡게", "카자흐스탄" }, { "QAR", "리얄", "카타르" }, { "KES", "실링", "케냐" },
			{ "COP", "페소", "콜롬비아" }, { "KWD", "디나르", "쿠웨이트" }, { "TZS", "실링", "탄자니아" }, { "TRY", "리라", "터어키" },
			{ "PKR", "루피", "파키스탄" }, { "PLN", "즈워티", "폴란드" }, { "HUF", "포린트", "헝가리" }, { "NPR", "루피", "네팔" },
			{ "MOP", "파타카", "마카오" }, { "KHR", "리엘", "캄보디아" }, { "FJD", "달러", "피지" }, { "LYD", "디나르", "리비아" },
			{ "RON", "레우", "루마니아" }, { "MMK", "짯", "미얀마" }, { "ETB", "비르", "에티오피아" }, { "UZS", "솜", "우즈베키스탄" }, };

	private static final HttpClient httpClient = HttpClient.newHttpClient();

	public static void main(String[] args) throws Exception {
		System.out.println("=================================================");
		System.out.println("      하나은행 환율 API 테스트");
		System.out.println("=================================================\n");

		// ── 테스트 1: USD 단건 조회 ──────────────────────
		System.out.println("▶ [테스트 1] USD 단건 조회");
		String usdResponse = callApi("USD", "0", "");
		System.out.println(usdResponse);
		printParsedRate(usdResponse);

		System.out.println("\n-------------------------------------------------\n");

		// ── 테스트 2: 전체 통화 조회 (CUR_CD 미입력) ──────
		System.out.println("▶ [테스트 2] 전체 통화 조회 (58종)");
		String allResponse = callApi("", "0", "");
		System.out.println(allResponse);

		System.out.println("\n-------------------------------------------------\n");

		// ── 테스트 3: 최초고시 조회 (PBLD_DV_CD=1) ────────
		System.out.println("▶ [테스트 3] JPY 최초고시 조회");
		String jpyResponse = callApi("JPY", "1", "");
		System.out.println(jpyResponse);
		printParsedRate(jpyResponse);

		System.out.println("\n=================================================");
		System.out.println("      테스트 완료");
		System.out.println("=================================================");
	}

	/**
	 * 환율 API 호출
	 *
	 * @param curCd    통화코드 (예: "USD", "" = 전체조회)
	 * @param pbldDvCd 고시구분코드 ("0"=최종고시, "1"=최초고시, "2"=고시회차)
	 * @param pbldSqn  고시회차 (pbldDvCd="2" 일 때만 사용, 나머지는 "" 입력)
	 */
	private static String callApi(String curCd, String pbldDvCd, String pbldSqn) throws Exception {

		// ── Request Body 구성 ──────────────────────────────
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("{");
		bodyBuilder.append("  \"dataHeader\": {");
		bodyBuilder.append("    \"ENTR_CD\": \"").append(ENTR_CD).append("\",");
		bodyBuilder.append("    \"CNTY_CD\": \"KR\",");
		bodyBuilder.append("    \"PROC_RSLT_DV_CD\": \"0\",");
		bodyBuilder.append("    \"CLNT_IP_ADR\": \"").append(CLNT_IP).append("\"");
		bodyBuilder.append("  },");
		bodyBuilder.append("  \"dataBody\": {");

		// CUR_CD: 비어있으면 전체조회이므로 필드 자체를 생략
		if (!curCd.isEmpty()) {
			bodyBuilder.append("    \"CUR_CD\": \"").append(curCd).append("\",");
		}
		bodyBuilder.append("    \"PBLD_DV_CD\": \"").append(pbldDvCd).append("\"");

		// 고시회차는 PBLD_DV_CD=2 일 때만 추가
		if (pbldDvCd.equals("2") && !pbldSqn.isEmpty()) {
			bodyBuilder.append(",    \"PBLD_SQN\": \"").append(pbldSqn).append("\"");
		}

		bodyBuilder.append("  }");
		bodyBuilder.append("}");

		String requestBody = bodyBuilder.toString();

		System.out.println("[요청 URL] POST " + API_URL);
		System.out.println("[요청 Body]\n" + prettyPrint(requestBody) + "\n");

		// ── HTTP 요청 ──────────────────────────────────────
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL))
				.header("Content-Type", "application/json; charset=utf-8").header("Accept", "application/json")
				.header("Authorization", AUTH_TOKEN).header("CRYP_DV_CD", "0") // 0=미적용
				.POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8)).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println("[응답 상태코드] " + response.statusCode());
		return response.body();
	}

	/**
	 * 응답 JSON에서 주요 환율 정보만 보기 좋게 출력 DEAL_BASC_RT 값은 16자리 고정소수 형식 → 실제 환율로 변환 예)
	 * "0010692000000000" → 1069.20 원
	 */
	private static void printParsedRate(String json) {
		System.out.println("\n[파싱된 환율 정보]");
		System.out.println("──────────────────────────────────────────");

		String curCd = extract(json, "CUR_CD");
		String curNm = extract(json, "CUR_NM");
		String cntyNm = extract(json, "CNTY_NM");
		String regDt = extract(json, "REG_DT");
		String pbldSqn = extract(json, "PBLD_SQN");
		String dealBascRt = extract(json, "DEAL_BASC_RT");
		String ttBuyRt = extract(json, "TT_BUY_RT");
		String ttSllRt = extract(json, "TT_SLL_RT");
		String acmnBuyRt = extract(json, "ACMN_BUY_RT");
		String acmnSllRt = extract(json, "ACMN_SLL_RT");

		System.out.printf("통화코드    : %s (%s / %s)%n", curCd, curNm, cntyNm);
		System.out.printf("고시일자    : %s  (회차: %s)%n", formatDate(regDt), pbldSqn);
		System.out.printf("매매기준율  : %s 원%n", parseRate(dealBascRt));
		System.out.printf("전신환매입율: %s 원  ← 해외송금 받을 때%n", parseRate(ttBuyRt));
		System.out.printf("전신환매도율: %s 원  ← 해외송금 보낼 때%n", parseRate(ttSllRt));
		System.out.printf("현찰매입율  : %s 원  ← 외화현금 팔 때%n", parseRate(acmnBuyRt));
		System.out.printf("현찰매도율  : %s 원  ← 외화현금 살 때%n", parseRate(acmnSllRt));
		System.out.println("──────────────────────────────────────────");
	}

	/**
	 * 16자리 고정소수 → 사람이 읽기 쉬운 환율 문자열로 변환 형식: 정수부 10자리 + 소수부 6자리 (소수점 없이 붙여서 저장) 예)
	 * "0010692000000000" → "1,069.20"
	 */
	private static String parseRate(String raw) {
		if (raw == null || raw.length() < 16)
			return "N/A";
		try {
			// 앞 10자리: 정수부, 뒤 6자리: 소수부
			long intPart = Long.parseLong(raw.substring(0, 10));
			long fracPart = Long.parseLong(raw.substring(10));
			// 소수부는 6자리 중 실제 소수 2자리만 사용 (나머지는 0)
			double rate = intPart + (fracPart / 10000.0 / 100.0);
			return String.format("%,.2f", rate);
		} catch (NumberFormatException e) {
			return raw;
		}
	}

	/** 날짜 포맷: "20180227" → "2018-02-27" */
	private static String formatDate(String raw) {
		if (raw == null || raw.length() != 8)
			return raw;
		return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6);
	}

	/** JSON 문자열에서 키에 해당하는 값 추출 (단순 파싱) */
	private static String extract(String json, String key) {
		String search = "\"" + key + "\"";
		int idx = json.indexOf(search);
		if (idx == -1)
			return "N/A";
		int colon = json.indexOf(":", idx);
		if (colon == -1)
			return "N/A";
		int start = json.indexOf("\"", colon) + 1;
		int end = json.indexOf("\"", start);
		if (start <= 0 || end == -1)
			return "N/A";
		return json.substring(start, end);
	}

	/** JSON 보기 좋게 들여쓰기 (단순 버전) */
	private static String prettyPrint(String json) {
		StringBuilder sb = new StringBuilder();
		int indent = 0;
		for (char c : json.toCharArray()) {
			if (c == '{' || c == '[') {
				sb.append(c).append("\n").append("  ".repeat(++indent));
			} else if (c == '}' || c == ']') {
				sb.append("\n").append("  ".repeat(--indent)).append(c);
			} else if (c == ',') {
				sb.append(c).append("\n").append("  ".repeat(indent));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
