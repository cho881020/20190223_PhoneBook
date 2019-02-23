package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import code.datas.PhoneNumber;
import code.datas.User;
import code.utils.GlobalData;

public class PhoneBookLauncher {
	
	public void startPhoneBook() {
		
		System.out.println("전화번호부를 시작합니다.");
		
		Scanner scan = new Scanner(System.in);
		
		printMainMenu();
//		0번을 누를때까지 무한반복
		
		while (true) {
			
			printMainMenu();
//			메뉴를 출력한 뒤 숫자를 입력받는다
			int userInput = scan.nextInt();	
			if (userInput == 1) {
//				로그인	
				login();
			}
			else if (userInput ==2) {
//				회원가입
				signUp();
			}
			else if (userInput ==0) {
				System.out.println("프로그램 종료");
				break;
			}
			else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요");
				
			}
		}
		
		
	}

//	메인화면을 출력하는 메쏘드
	public void printMainMenu() {
		System.out.println("********** 메인화면 **********");
		System.out.println(" 1.로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 0. 프로그램 종료");
		System.out.println("****************************");
		System.out.println("원하는 동작을 입력 : ");
		
	}
	
//	아이디,비번 입력받아서 DB에 로그인시도
	public void login() {
//		아이디 입력받아서 저장
		Scanner scan1 = new Scanner(System.in);
		System.out.print("ID(Email) : ");
		String userInputEmail = scan1.nextLine();
		
//		비밀번호 입력받아서 저장
		Scanner scan2 = new Scanner(System.in);
		System.out.print("Password : ");
		String userInputPassword = scan2.nextLine();
		
//		JDBC를 통해서 DB서버에 실제 사용자가 맞는지 문의 .Query
//		별개의 메쏘드가 수행한 결과를 로그인결과 변수에 저장.
		boolean loginResult = findUserInDatabase(userInputEmail, userInputPassword);
		
		if (loginResult) {
//			로그인 성공! 성공처리 메쏘드 별개로 작성
			System.out.print("로그인에 성공했습니다!");
			System.out.print(String.format("%s님 환영합니다!", GlobalData.loginUser.getName()));
			
			loginMenu();
			
			
		}
		else {
			System.out.println("아이디나 비번이 잘못되었습니다.");
			System.out.println("메인화면으로 이동합니다.");
		}
	}
	
//	아이디,비번을 가지고 실제로 DB에 있는지 쿼리를 날리는 메쏘드
//	있으면 true, 없으면 false 를 리턴
	public boolean findUserInDatabase(String email, String pw) {
		boolean result = false;
		
//		DB와의 연결 상태를 저장하는 변수
		Connection conn = null;
//		연결된 디비에 쿼리를 실행시켜줌
		Statement stmt = null;
//		쿼리 실행 결과를 저장하는 변수 (표)
		ResultSet rs = null;
		

		try {
//			JDBC 불러오기
			Class.forName("com.mysql.jdbc.Driver");
//			불러온 JDBC 이용해 디비 접속
//			접속 정보를 변수에 저장
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속정보로 디비 접속
			conn = DriverManager.getConnection(url, "delivery","dbpassword");
			
//			System.out.println("DB연결 성공!");
			
//			재료로 받은 아이디,비번이 모두 맞는 사용자가 있는지 쿼리
			String loginQuery = String.format("SELECT * FROM users WHERE email = '%s' AND password = '%s';", email,pw);
			
//			쿼리를 수행해서 결과를 저장
//			1. stmt 변수를 객체화
			stmt = conn.createStatement();
//			2.stmt이용해서 로그인 쿼리 실행 + 결과를 rs에 저장
			rs = stmt.executeQuery(loginQuery);
//			3.rs에 저장된 표를 조회
//			rs.next는, 다음 읽을 줄이 있다면 그 줄로 커서를 이동시키고 true 리턴
//			읽을 내용이 더 없다면 false리턴
			while(rs.next()) {
//				이안에 들어왔다 = 아이디,비번이 맞는 사람이 있다
//				=>로그인에 성공했다!
				
				result = true;
				
//				로그인한 사람을 객체로 만들어서 GlobalData의 변수에 저장
				User tempUser = new User();
//				쿼리 결과에서 두번째 컬럼이 이름이니, 이를 스트링으로 뽑아서 저장
				tempUser.setName(rs.getString(rs.findColumn("name")));
				tempUser.setEmail(rs.getString(rs.findColumn("email")));
//				로그인한 사람을 저장
				GlobalData.loginUser = tempUser;
				
				
			}
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public void signUp() {
//		회원가입 관련 코드 작성될 부분
//		사용자에게 이메일,비번,이름 입력받아서 db에 INSERT!
		System.out.println("회원가입을 진행합니다.");
		
		Scanner scan1 = new Scanner(System.in);
		System.out.print("아이디를 입력하세요 : ");
		String signUpEmail = scan1.nextLine();
		
		Scanner scan2 = new Scanner(System.in);
		System.out.print("비밀번호를 입력하세요 : ");
		String signUpPassword = scan2.nextLine();
		
		Scanner scan3 = new Scanner(System.in);
		System.out.print("이름을 입력하세요 : ");
		String signUpName = scan3.nextLine();
		
		registerUserToDB(signUpEmail, signUpPassword, signUpName);
		
		
	}
//	db에 사용자 등록
	public void registerUserToDB(String email,String pw,String name) {
		
		Connection conn = null;
		
//		insert 등의 데이터 조작 쿼리를 실행시켜주는 변수
		PreparedStatement pstmt = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			conn = DriverManager.getConnection(url, "delivery","dbpassword");
			
//			insert쿼리 작성
			String signUpSql = String.format("INSERT INTO users (email,password,name) VALUES ('%s','%s','%s');", email, pw, name);
			
			
//			db연결이 되었으니 ,insert쿼리 날릴 준비
			pstmt = conn.prepareStatement(signUpSql);
			
//			insert/update/delete문 실행하면, 영향받은줄이 몇줄인지? 
			int affectedRowCount = pstmt.executeUpdate();
			
			if (affectedRowCount == 0) {
				System.out.println("회원가입에 문제가 발생했습니다");
				
			}
			else {
				System.out.println("회원가입 성공!");
				System.out.println("메인메뉴로 돌아갑니다");
				
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void loginMenu() {
		
		Scanner scan = new Scanner(System.in);
		while (true) {
			printUserMenu();
			int menuNum = scan.nextInt();
			
			if (menuNum == 1) {
				
			}
			else if (menuNum == 2) {
				addPhoneNumber();
			}
			else if (menuNum == 3) {
				
			}
			
			else if (menuNum == 0) {
//				로그아웃 => 로그인한 사람이 없다고 설정
				GlobalData.loginUser = null;
				
				System.out.println("로그아웃했습니다.");
				System.out.println("메인화면으로 돌아갑니다.");
				break;
				
			}
			else {
				System.out.println("잘못된 입력입니다");
			}
		}
		
	}
	
	
	public void printUserMenu() {
		System.out.println("********** 전화번호부 **********");
		System.out.println(" 1. 전체 전화번호 목록 조회");
		System.out.println(" 2. 전화번호 등록");
		System.out.println(" 3. 전화번호 삭제");
		System.out.println(" 0. 로그아웃");
		System.out.println("****************************");
		System.out.print("메뉴를 선택하세요 : ");
		
	}
	
	public void showAllPhoneNumbers() {
//		전화번호 전체 목록 조회 => 로그인한 사람이 입력한 전화번호만.
//		동작 구조 : DB에서 먼저 목록을 조회
//		조회 결과를 ArrayList에 저장
//		저장된 ArrayList를 화면에 출력
		GlobalData.loginUserPhoneNumbers = getPhoneNumsFromDB();
//		저장된 ArrayList를 화면에 출력
		for (int i = 0; i < GlobalData.loginUserPhoneNumbers.size; i++) {
			PhoneNumber pn = GlobalData.loginUserPhoneNumbers.get(i);
			
						System.out.println(String.format("%s/%s/%s", pn.getName(),pn.getPhoneNumber(),pn.getMemo());
		}
	}
	
//	db에 저장된 내가 저장한 폰번들을 가져옴
	public List<PhoneNumber> getPhoneNumsFromDB() {
		
//		찾아낸 폰번들을 임시로 저장할 ArrayList
		List<PhoneNumber> tempPhoneNumbers = new ArrayList<PhoneNumber>();
		
//		DB와의 연결 상태를 저장하는 변수
		Connection conn = null;
//		연결된 디비에 쿼리를 실행시켜줌
		Statement stmt = null;
//		쿼리 실행 결과를 저장하는 변수 (표)
		ResultSet rs = null;
		

		try {
//			JDBC 불러오기
			Class.forName("com.mysql.jdbc.Driver");
//			불러온 JDBC 이용해 디비 접속
//			접속 정보를 변수에 저장
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속정보로 디비 접속
			conn = DriverManager.getConnection(url, "delivery","dbpassword");
			
//			System.out.println("DB연결 성공!");
			
//			로그인한 사람이 등록한 폰번들을 조회하는 쿼리  Ex. 7번 사용자가 등록한 모든 폰번?
			String phoneNumsQuery = String.format("SELECT * FROM phone_numbers WHERE user_id=%d;", GlobalData.loginUser.getId());
			
//			쿼리를 수행해서 결과를 저장
//			1. stmt 변수를 객체화
			stmt = conn.createStatement();
//			2.stmt이용해서 로그인 쿼리 실행 + 결과를 rs에 저장
			rs = stmt.executeQuery(phoneNumsQuery);
//			3.rs에 저장된 표를 조회
//			rs.next는, 다음 읽을 줄이 있다면 그 줄로 커서를 이동시키고 true 리턴
//			읽을 내용이 더 없다면 false리턴
			
			
			
			
			while(rs.next()) {
//				이안에 들어왔다 = 내가 폰번을 저장한게 있다
//				=>전부 찾아서 저장!
				
				
				
//				하나하나를 [전화번호 목록]으로 변환해서 저장
				PhoneNumber tempPN = new PhoneNumber();
				

				tempPN.setId(rs.getInt(rs.findColumn("id")));
				tempPN.setName(rs.getString(rs.findColumn("id")));
				tempPN.setPhoneNumber(rs.getString(rs.findColumn("phone_num")));
				tempPN.setMemo(rs.getString(rs.findColumn("memo")));
				
//				찾아낸 폰번을 저장. => 만들어진 ArrayList<PhoneNumber>에 추가
				
				tempPhoneNumbers.add(tempPN);
				
				
			}
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempPhoneNumbers;
	}
	
	
//	전화번호 등록을 누르면 들어오는 메쏘드
	public void addPhoneNumber() {
		
		System.out.println("새 전화번호를 등록합니다.");
		Scanner scan = new Scanner(System.in);
		System.out.print("이름 : ");
		String inputName = scan.next();
		
		
		System.out.print("폰번 : ");
		String inputPhoneNum = scan.next();
		
		System.out.print("특이사항 : ");
		String inputMemo = scan.next();
//		여기까지 사용자에게 정보 입력받는 부분
		
//		입력을 다 받았으니 db에 기록
		registerPhoneNumToDB(inputName, inputPhoneNum, inputMemo);
	}
	
	public void registerPhoneNumToDB(String inputName, String inputPhoneNum, String inputMemo) {
		Connection conn = null;
//		연결된 디비에 쿼리를 실행시켜줌
		Statement stmt = null;
//		쿼리 실행 결과를 저장하는 변수 (표)
		ResultSet rs = null;
		

		try {
//			JDBC 불러오기
			Class.forName("com.mysql.jdbc.Driver");
//			불러온 JDBC 이용해 디비 접속
//			접속 정보를 변수에 저장
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속정보로 디비 접속
			conn = DriverManager.getConnection(url, "delivery","dbpassword");
			
//			System.out.println("DB연결 성공!");
			
//			재료로 받은 아이디,비번이 모두 맞는 사용자가 있는지 쿼리
			String loginQuery = String.format("SELECT * FROM users WHERE email = '%s' AND password = '%s';", email,pw);
			
//			쿼리를 수행해서 결과를 저장
//			1. stmt 변수를 객체화
			stmt = conn.createStatement();
//			2.stmt이용해서 로그인 쿼리 실행 + 결과를 rs에 저장
			rs = stmt.executeQuery(loginQuery);
//			3.rs에 저장된 표를 조회
//			rs.next는, 다음 읽을 줄이 있다면 그 줄로 커서를 이동시키고 true 리턴
//			읽을 내용이 더 없다면 false리턴
			while(rs.next()) {
//				이안에 들어왔다 = 아이디,비번이 맞는 사람이 있다
//				=>로그인에 성공했다!
				
				result = true;
				
//				로그인한 사람을 객체로 만들어서 GlobalData의 변수에 저장
				User tempUser = new User();
//				쿼리 결과에서 두번째 컬럼이 이름이니, 이를 스트링으로 뽑아서 저장
				tempUser.setName(rs.getString(rs.findColumn("name")));
				tempUser.setEmail(rs.getString(rs.findColumn("email")));
				
				tempUser.setId(rs.getInt(rs.findColumn("id")));
				
//				로그인한 사람을 저장
				GlobalData.loginUser = tempUser;
				
				
			}
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public void signUp() {
//		회원가입 관련 코드 작성될 부분
//		사용자에게 이메일,비번,이름 입력받아서 db에 INSERT!
		System.out.println("회원가입을 진행합니다.");
		
		Scanner scan1 = new Scanner(System.in);
		System.out.print("아이디를 입력하세요 : ");
		String signUpEmail = scan1.nextLine();
		
		Scanner scan2 = new Scanner(System.in);
		System.out.print("비밀번호를 입력하세요 : ");
		String signUpPassword = scan2.nextLine();
		
		Scanner scan3 = new Scanner(System.in);
		System.out.print("이름을 입력하세요 : ");
		String signUpName = scan3.nextLine();
		
		registerUserToDB(signUpEmail, signUpPassword, signUpName);
		
		
	}
//	db에 사용자 등록
	
	/*
	 * public void registerPhoneNumToDB(String email,String pw,String name) {
	 * 
	 * Connection conn = null;
	 * 
	 * // insert 등의 데이터 조작 쿼리를 실행시켜주는 변수 PreparedStatement pstmt = null;
	 * 
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver"); String url =
	 * "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
	 * conn = DriverManager.getConnection(url, "delivery","dbpassword");
	 * 
	 * // insert쿼리 작성 String signUpSql = String.
	 * format("INSERT INTO phone_numbers (name,phone_num,memo,user_id) VALUES ('%s','%s','%s',%d);"
	 * , inputName, inputPhone ,inputMemo, GlobalData.loginUser.getName());
	 * 
	 * 
	 * // db연결이 되었으니 ,insert쿼리 날릴 준비 pstmt = conn.prepareStatement(signUpSql);
	 * 
	 * // insert/update/delete문 실행하면, 영향받은줄이 몇줄인지? int affectedRowCount =
	 * pstmt.executeUpdate();
	 * 
	 * if (affectedRowCount == 0) { System.out.println("회원가입에 문제가 발생했습니다");
	 * 
	 * } else { System.out.println("회원가입 성공!"); System.out.println("메인메뉴로 돌아갑니다");
	 * 
	 * }
	 * 
	 * 
	 * } catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (SQLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } }
	 */
	
	
}
