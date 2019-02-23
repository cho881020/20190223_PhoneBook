package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import code.data.User;
import code.utils.GlobalData;

public class PhoneBookLauncher {

	public void startPhoneBook() {
		System.out.println("전화번호부를 시작합니다.");

		Scanner scan = new Scanner(System.in);

//		0번을 누룰 때 까지 무한 반복 => while

		while (true) {
			printMainMenu();
//			메뉴를 출력한 뒤 숫자를 입력받는다.			
			int userInput = scan.nextInt();

			if (userInput == 1) {
//				로그인
				login();
			} else if (userInput == 2) {
//				회원가입
				signUp();

			} else if (userInput == 0) {
//				프로그램 종료
				System.out.println("프로그램을 종료합니다.");
				break;
			} else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}

		}

	}

//	메인화면을 출력하는 메소드
	public void printMainMenu() {

		System.out.println("********** 전화번호부 **********");
		System.out.println(" 1. 로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 0. 프로그램 종료");
		System.out.println("****************************");
		System.out.print("원하는 동작을 입력 : ");

	}
	
//	아이디, 비번을 입력받아서 DB에 로그인 시도
	
	public void login() {
		
		Scanner scan1 = new Scanner(System.in);
		
		System.out.print("ID(Email) : ");
		String userInputEmail = scan1.nextLine();
		
		Scanner scan2 = new Scanner(System.in);
		System.out.print("Password : ");
		String userInputPassword = scan2.nextLine();
		
//		JDBC를 통해서 DB서버에 실제 사용자가 맞는지 문의. Query
//		별개의 메소드가 수행한 결과를 로그인겨로가 변수에 저장
		boolean loginResult = findUserInDatabase(userInputEmail, userInputPassword);
		
		if(loginResult) {
//			로그인 성공! 성공 처리 메소드 별개로 작성
			
			System.out.println("로그인에 성공했습니다.");
			System.out.println(String.format("'%s님 환영합니다!", GlobalData.loginUser.getName()));
		}else {
			System.out.println("아이디나 비번이 잘못되었습니다.");
			System.out.println("메인 화면으로 이동합니다.");
		}
		
	}

	
//	아이디, 비번을 가지고 실제로 DB에 있는지 쿼리를 날리는 메쏘드
//	있으면 true, 없으면 false를 리턴
	
	public boolean findUserInDatabase(String email, String pw) {
		
		boolean result = false;
		
//		DB와의 연결 상태를 저장해두는 변수
		Connection conn = null;
//		연결된 DB에 쿼리를 실행시켜줌
		Statement stmt = null;
//		쿼리 실행 결과를 저장하는 변수. (표)
		ResultSet rs = null;
		
//		JDBC를 불러오기.
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
//			불러온 JDBC를 이용해서 DB에 접속
//			접속 정보를 변수에 저장
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속 정보와 아이디 비번을 가지고 실제로 DB에 접속
			conn = DriverManager.getConnection(url,"delivery","dbpassword");
			

//			재료로 받은 아이디/비번이 모두 맞는 사용자가 있는지 쿼리.
			String loginQuery = String.format("SELECT * FROM users"
					+"WHERE email = '%s' AND password = '%s';",email , pw);
			
//			쿼리를 수행해서 결과를 저장
//			1. stmt 변수를 객체화
			stmt = conn.createStatement();
			
//			2. stmt를 이용해서 loginQuery를 실행 + 결과를 rs에 저장
			rs = stmt.executeQuery(loginQuery);
			
//			3. rs에 저장된 표를 조회
//			rs.next()는, 다음 읽을 줄이 있다면 그 줄로 커서를 이동. true
//			읽을 내용이 더 없다면 그냥 false를 리턴
			while(rs.next()) {
//				이 안에 들어왔다 => 아이디 비번이 맞는 사람이 있다!
//				=> 로그인에 성공!
				
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
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	public void signUp() {
//		회원가입 관련 코드가 작성될 부분
//		사용자에게 이메일, 비번, 이름을 입력받아서 DB에 INSERT!
		
		System.out.println("회원가입을 진행합니다.");
		
		
		Scanner scan1 = new Scanner(System.in);
		System.out.print("아이디 입력 : ");
		String signUpEmail = scan1.nextLine();
		
		Scanner scan2 = new Scanner(System.in);
		System.out.print("비밀번호 입력 : ");
		String signUpPassword = scan2.nextLine();
		
		Scanner scan3 = new Scanner(System.in);
		System.out.print("이름 입력 : ");
		String signUpName = scan3.nextLine();
		
		registerUserToDB(signUpEmail, signUpPassword, signUpName);
		
	}
	
//	DB에 사용자를 등록!
	public void registerUserToDB(String email, String pw,String name) {
		
		Connection conn = null;

//		INSERT 등의 데이터 조작 쿼리를 실행시켜주는 변수
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			conn = DriverManager.getConnection(url,"delivery","dbpassword");
			
//			INSERT 쿼리를 먼저 작성!
			String signUpSql = String.format("INSERT INTO users(email, password,name) "
					+ "VALUES ('%s', '%s', '%s');", email, pw, name);
			
//			DB연결이 되었으니, INSERT 쿼리를 날릴 준비
			pstmt = conn.prepareStatement(signUpSql);
		
//			INSERT/UPDATE/DELETE 문을 실행하면, 영향 받은 줄이 몇줄인지 affectedRowCount에 저장됨
			int affectedRowCount = pstmt.executeUpdate();
			
			if(affectedRowCount == 0) {
				System.out.println("회원가입에 문제가 발생했습니다.");
			}else {
				System.out.println("회원가입에 성공했습니다.");
				System.out.println("메인메뉴로 돌아갑니다.");
			}
						
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}











