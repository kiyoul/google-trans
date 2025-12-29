# Google Translate REST API

Spring Boot 기반의 간단한 REST API로, Google Cloud Translation API를 호출하여 텍스트를 번역합니다.

## 사전 준비
- JDK 17 이상
- Maven 3.9 이상 (`apache-maven-3.9.11`을 사용해도 됩니다)
- Google Cloud 프로젝트와 Translation API 사용 설정
- 서비스 계정을 만들고 JSON 키 파일을 내려받은 뒤 `GOOGLE_APPLICATION_CREDENTIALS` 환경 변수에 경로를 지정하세요.

## 설정
1. `src/main/resources/application.yml`의 `google.cloud.project-id` 값을 실제 프로젝트 ID로 수정합니다.
2. (옵션) `google.cloud.location`을 `global` 외의 리전에 두고 싶다면 값을 변경합니다.

## 실행
```bash
mvn spring-boot:run
```
혹은 패키징 후 실행할 수 있습니다.
```bash
mvn clean package
java -jar target/google-trans-0.0.1-SNAPSHOT.jar
```

## API 사용 예시
POST `http://localhost:8080/api/translate`
```json
{
  "sourceLanguage": "en",
  "targetLanguage": "ko",
  "text": "Google Cloud Translation makes it easy."
}
```
응답:
```json
{
  "translatedText": "구글 클라우드 번역은 쉽게 만들어줍니다.",
  "detectedSourceLanguage": "en",
  "targetLanguage": "ko"
}
```

간단한 상태 확인은 `GET /api/translate/health` 로 가능합니다.
# google-trans
