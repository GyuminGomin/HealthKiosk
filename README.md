# 초기 셋팅

## Java 17 설치
1. 사이트 접속 후 설치 및 세팅 : <a href="https://www.oracle.com/kr/java/technologies/downloads/#jdk17-windows">OpenJDK-17버전 window 패키지 설치</a>

- 다음 과정을 따라하시면 됩니다.

<img src="./README_Img/JDK17/1.png">

---

<img src="./README_Img/JDK17/2.png">

---

<img src="./README_Img/JDK17/3.png">

---

<img src="./README_Img/JDK17/4.png">

---

<img src="./README_Img/JDK17/5.png">

---

<img src="./README_Img/JDK17/6.png">

---

<img src="./README_Img/JDK17/7.png">

---

<img src="./README_Img/JDK17/8.png">

---

<img src="./README_Img/JDK17/9.png">

---

<img src="./README_Img/JDK17/10.png">

---

<img src="./README_Img/JDK17/11.png">

---

<img src="./README_Img/JDK17/12.png">

- 설정완료 후 cmd(명령프롬프트) 창을 켜서 

<img src="./README_Img/JDK17/13.png">

- 완료

## Eclipse 설치
1. 사이트 접속 후 설치 및 세팅 : <a href="https://www.eclipse.org/downloads/packages/">eclipse 패키지 설치</a>

- 다음 과정을 따라하시면 됩니다.

<img src="./README_Img/Eclipse/1.png">

- zip파일 다운로드 클릭

- 다운로드 받은 파일 C://Program Files 폴더 밑에다가 압축풀기

- eclipse.exe 파일 바로가기 바탕화면으로 옮기기

- eclipse.exe 실행

- 완료

## JavaFX 설치

1. 사이트 접속 후 설치 및 세팅 : <a href="https://gluonhq.com/products/javafx/">javafx gluon 설치</a>

- 다음 과정을 따라하시면 됩니다.

```
- Downloads 페이지에서

JavaFX Version : 17.0.9 [LTS]
Operating System : Windows
Architecture : x64
Type : SDK
```

- C:/Program Files/Java 폴더 아래에 압축 해제

### eclipse 실행 후

- 다음 과정을 따라하시면 됩니다.

- 먼저 프로젝트를 진행할 workspace를 생성합니다. (아무위치)

- 그 위치에 workspace를 지정하고 eclipse 실행

<img src="./README_Img/Eclipse/2.png">

---

<img src="./README_Img/Eclipse/3.png">

---

<img src="./README_Img/Eclipse/4.png">

---

<img src="./README_Img/Eclipse/5.png">

---

<img src="./README_Img/Eclipse/6.png">

---

<img src="./README_Img/Eclipse/7.png">

- Finish를 하고 나면 Eclipse 오른쪽 아래나 윈도우 창의 이클립스 아이콘에 `installing software`이라는 표시가 끝날때까지 기다려야 합니다. (초큼 시간 걸림)

<img src="./README_Img/Eclipse/8.png">

- 이 다운로드 시간 동안 scene builder 설치 권장합니다.

---

<img src="./README_Img/Eclipse/9.png">

---

<img src="./README_Img/Eclipse/10.png">

---

<img src="./README_Img/Eclipse/11.png">

- 이제 여기까지 진행했으면, 기존 health_kiosk 프로젝트 폴더는 삭제 (실제로 할것은 javafx 프로젝트이므로)

---

<img src="./README_Img/Eclipse/12.png">

---

<img src="./README_Img/Eclipse/13.png">

- 현재 쓰지 않는 Maven은 체크해제, JavaFX 체크

- 세부 설정
<img src="./README_Img/Eclipse/14.png">
<img src="./README_Img/Eclipse/15.png">

---

<img src="./README_Img/Eclipse/16.png">

---

<img src="./README_Img/Eclipse/17.png">

---

<img src="./README_Img/Eclipse/18.png">

---

<img src="./README_Img/Eclipse/19.png">

- 완료

## Scene Builder 설치

1. 사이트 접속 후 설치 및 세팅 : <a href="https://gluonhq.com/products/scene-builder/">scene builder gluon 설치</a>

- 다음 과정을 따라하시면 됩니다.

```
Product : Scene Builder
Platform : Windows Installer
```

- msi 파일 실행 후

<img src="./README_Img/Eclipse/20.png">

---

<img src="./README_Img/Eclipse/21.png">

---

<img src="./README_Img/Eclipse/22.png">

---

<img src="./README_Img/Eclipse/23.png">

---

<img src="./README_Img/Eclipse/24.png">

---

- 위치 C:\Users\사용자\AppData\Local\SceneBuilder\SceneBuilder.exe

<img src="./README_Img/Eclipse/25.png">

- 완료

## JDBC 설치

`DB 연동하는 작업은 제가 할 것 이기 때문에 mysql은 집에 안 깔아두셔도 될 것 같아요 - 만약 그래도 깔고 싶으시다면 집에 mysql workbench 꼭 깔아두시고 jdbc 설치를 진행해주세요. mysql 설치는 인터넷에 검색하면 많이 나옵니다`

1. 사이트 접속 후 설치 및 세팅 : <a href="https://dev.mysql.com/downloads/">mysql Connector/J 설치</a>

- 다음 과정을 따라하시면 됩니다.

<img src="./README_Img/JDBC/1.png">

---

<img src="./README_Img/JDBC/2.png">

---

<img src="./README_Img/JDBC/3.png">

- 다운 받은 파일을 C://Program Files/MySQL 폴더 아래에 압축 해제
---

- 이제 이클립스 실행 후, 빌드패스 설정

<img src="./README_Img/JDBC/4.png">

---

<img src="./README_Img/JDBC/5.png">

---

<img src="./README_Img/JDBC/6.png">

---

<img src="./README_Img/JDBC/7.png">

- 완료

