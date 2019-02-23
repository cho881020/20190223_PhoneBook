package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import code.datas.User;
import code.utils.GlobalData;

public class PhoneBookLauncher {

	public void startPhoneBook() {
		System.out.println("전화번호부를 시작합니다.");		
		Scanner scan = new Scanner(System.in);				
		
//		0번 누를때 까지 무한 반복 =>while		
		while(true) {
			printMainMenu();
//			메뉴를 출력한 뒤 숫자를 입력받는다.
			int userInput = scan.nextInt();
			
			if(userInput == 1) {
//				로그인 => 다른 메쏘드로 이동(login())
				login();				
			}
			
			else if(userInput == 2) {
//				회원가입
				signUp();				
			}
			
			else if(userInput == 0) {				
//				프로그램 종료
				System.out.println("프로그램을 종료합니다.");
				break;
			}
			else System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
		} // while(메뉴출력)
		
		
	}
	
//	메인화면 출력하는 메쏘드
	public void printMainMenu() {
		
		System.out.println("********** 메인 화면 **********");
		System.out.println(" 1. 로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 0. 프로그램 종료");
		System.out.println("*******************************");
		System.out.print(" 원하는 동작 입력 : ");
		
		
	} // 프린트 메쏘드
	
	 
//	아이디, 비번 입력받아서 DB에 로그인 시도
	public void login() {
		
//		아이디를 입력받아서 저장
		Scanner scan1 = new Scanner(System.in);
		System.out.print("ID(Eamil) : ");
		String userInputEmail = scan1.nextLine();
		
//		비밀번호도 입력받아서 저장
		Scanner scan2 = new Scanner(System.in);
		System.out.print("Password : ");
		String userInputPassword = scan2.nextLine();
		
//		JDBC를 통해 DB 서버에 실제 사용자가 맞는지 문의. Query
//		별개의 메쏘드가 수행한 결과를 로그인결과 변수에 저장.
		boolean loginResult = findUserInDatabase(userInputEmail, userInputPassword);
		
		if(loginResult) {
//			로그인 성공! 성공처리 메쏘드 별개로 작성.
			System.out.println("로그인에 성공했습니다.");
			System.out.println(String.format("%s님 환영합니다.", GlobalData.loginUser.getName()));
			
			loginMenu();
			
		}
		
		else {
			System.out.println("아이디나 비번이 잘못되었습니다.");
			System.out.println("메인화면으로 이동합니다.");
		}
		
	}
	
//	아이디, 비번을 가지고 실제로 DB에 있는지 쿼리를 날리는 메쏘드.
//	있으면 true, 없으면 false를 리턴.
	
	public boolean findUserInDatabase(String email, String pw) {
		boolean result = false;
		
//		DB와의 연결 상태를 저장해두는 변수
		Connection conn = null;		
//		연결된 DB에 쿼리를 실행시켜줌.
		Statement stmt = null;		
//		쿼리 실행 결과를 저장하는 변수. (표)
		ResultSet rs = null;
		
		try {
//			JDBC를 불러오기.
			Class.forName("com.mysql.jdbc.Driver");
			
//			불러온 JDBC를 이용해서 DB에 접속
//			접속 정보를 변수에 저장.
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속 정보와 아이디 비번을 가지고 실제로 DB에 접속.			
			conn = DriverManager.getConnection(url,"delivery","dbpassword");
			
//			재료로 받은 아이디/비번이 모두 맞는지 사용자가 있는지 쿼리.
			String loginQuery = String.format("SELECT * FROM users "
					+ "WHERE email = '%s' AND password = '%s';", email, pw);
			
//			쿼리를 수행해서 결과를 저장
//			1. stmt 변수를 객체화
			stmt = conn.createStatement();
			
//			2. stmt를 이용해서 loginQuery를 실행 + 결과를 rs에 저장
			rs = stmt.executeQuery(loginQuery);
			
//			3. rs에 저장된 표를 조회
//			rs.next는 다음 읽을 줄이 있다면 그 줄로 커서를 이동. true
//			읽을 내용이 더 없다면 그냥 false를 리턴.
			while(rs.next()) {
//				이안에 들어왔다 => 아이디 비번이 맞는 사람이 있다!
//				 => 로그인에 성공!
				result = true;
				
				User tempUser = new User();
				
//				쿼리 결과에서 두번째 컬럼이 이름이니, 이를 스트링으로 뽑아서 저장.
				
				tempUser.setName(rs.getString(rs.findColumn("name")));
				tempUser.setEmail(rs.getString(rs.findColumn("email")));
				
//				누락 되었던 사용자 고유번호(id)도 세팅
				tempUser.setId(rs.getInt(rs.findColumn("id")));
				
				
//				로그인 한 사람을 저장!
				GlobalData.loginUser = tempUser;	
				
			}				
								
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DB 드라이버 로딩 실패");
			System.out.println("JDBC를 제대로 추가했는지 확인해주세요.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DB 서버 연결 실패");
			System.out.println("DB 서버가 접속이 가능한 상태인지 확인.");
			System.out.println("접속 정보를 틀리지 않고 입력했는지 코드 검토.");
		}
		
		
		return result;
	}
	
	public void signUp() {
//		회원가입 관련 코드가 작성될 부분.
//		사용자에게 이메일, 비번, 이름을 입력받아서 DB에 INSERT!
		
		System.out.println("회원가입을 진행합니다.");
		
		Scanner scan1 = new Scanner(System.in);
		System.out.print("아이디를 입력 : ");
		String sighUpEmail = scan1.nextLine();
		
		Scanner scan2 = new Scanner(System.in);
		System.out.print("비밀번호를 입력 : ");
		String sighUpPassword = scan2.nextLine();
		
		Scanner scan3 = new Scanner(System.in);
		System.out.print("이름를 입력 : ");
		String sighUpName = scan3.nextLine();
		
		registerUserToDB(sighUpEmail, sighUpPassword, sighUpName);
		
	}
	
//	DB에 사용자를 등록!
	public void registerUserToDB(String email, String pw, String name) {
		
		Connection conn = null;
//		INSERT 등의 데이터 조작 쿼리를 실행시켜주는 변수
		PreparedStatement pstmt = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
			conn = DriverManager.getConnection(url, "delivery", "dbpassword");
		
//			INSERT 쿼리를 먼저 작성!
			String signUpSql = String.format("INSERT INTO users (email, password, name) "
					+ "VALUES ('%s', '%s', '%s');", email, pw, name );
			
			
//			DB연결이 되었으니, INSERT 쿼리를 날릴 준비.						
			pstmt = conn.prepareStatement(signUpSql);
			
//			INSERT/UPDATE/DELET 문을 실행하면, 영향 받은 줄이 몇줄인지?
			int affectedRowCount = pstmt.executeUpdate();
			
			if(affectedRowCount == 0) {
				System.out.println("회원가입에 문제가 발생했습니다.");
			}
			else {
				System.out.println("회원가입에 성공했습니다!");
				System.out.println("메인메뉴로 돌아갑니다.");
				System.out.println();
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
		
		while(true) {
		
			printUserMenu();
			int menuNum = scan.nextInt();
			
			if(menuNum == 1) {
				
			}
			else if(menuNum == 2) {
				addPhoneNumber();
			}
			else if(menuNum == 3) {
				deletePhoneNum();
				
			}
			else if(menuNum == 0) { 
//				로그아웃 => 로그인 한 사람이 없다고 설정
				
				GlobalData.loginUser = null;
				System.out.println("로그아웃했습니다.");
				System.out.println("메인 화면으로 돌아갑니다.");		
				System.out.println();
				break;
			}
			else {
				System.out.println("잘못된 입력입니다.");
			}
			
		}
		
	}
	
	
	public void printUserMenu() {
		
		System.out.println("********** 전화번호부 **********");
		System.out.println(" 1. 전체 전화번호 목록 조회");
		System.out.println(" 2. 전체 전화번호 등록");
		System.out.println(" 3. 전체 전화번호 삭제");
		System.out.println(" 0. 로그아웃");
		System.out.println("*******************************");
		System.out.print("메뉴를 선택하세요 : ");
		
		
		
	}
	
	public void showAllPhoneNumbers() {
		
//		
		
	}
	
//	전화번호 등록을 누르면 들어오는 메쏘드
	public void addPhoneNumber() {
		
		System.out.println("새 전화번호를 등록합니다.");
		
		Scanner scan = new Scanner(System.in);
		System.out.print("이름 : ");
		String inputName = scan.next();
		
		System.out.print("폰번 : ");
		String inputPhoneNum= scan.next();
		
		System.out.print("특이사항 : ");
		String inputMemo = scan.next();
		
//		여기까지 사용자에게 정보를 입력 받는 부분
		
//		입력을 다 받았으니 DB에 기록 => 별개 메쏘드
		registerPhoneNumToDB(inputName, inputPhoneNum, inputMemo);
		
		
	}
	
//	실제로 데이터베이스에 전화번호 입력사항을 기록해줌.
	public void registerPhoneNumToDB(String inputName, String inputPhone, String inputMemo) {
		
		
		Connection conn = null;
//		INSERT 등의 데이터 조작 쿼리를 실행시켜주는 변수
		PreparedStatement pstmt = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";			
			conn = DriverManager.getConnection(url, "delivery", "dbpassword");
		
//			INSERT 쿼리를 먼저 작성!
			String insertPhoneNumSql = String.format("INSERT INTO phone_numbers (name, phone_num, memo, user_id) "
					+ "VALUES ('%s', '%s', '%s', '%d');", inputName, inputPhone, inputMemo, GlobalData.loginUser.getId());
			
			
//			DB연결이 되었으니, INSERT 쿼리를 날릴 준비.						
			pstmt = conn.prepareStatement(insertPhoneNumSql);
			
//			INSERT/UPDATE/DELET 문을 실행하면, 영향 받은 줄이 몇줄인지?
			int affectedRowCount = pstmt.executeUpdate();
			
			if(affectedRowCount == 0) {
				System.out.println("전화번호 등록에 문제가 발생했습니다.");
			}
			else {
				System.out.println("전화번호 등록에 성공했습니다!");
				System.out.println("사용자 메뉴로 돌아갑니다.");
				System.out.println();
			}
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void deletePhoneNum() {
		
//		사용자에게 삭제하고 하는 사람의 이름을 입력받자.
		
		System.out.println("전화번호 삭제!");
		
		Scanner scan = new Scanner(System.in);
		System.out.println("지울 사람 이름 입력 : ");
		String deletephoneNumName = scan.next();
		
		deletePhoneNumFromDB(deletephoneNumName);
		
//		(로그인한 사용자가 등록한 폰번중) 그 이름과 같은 폰번을 삭제.
//		삭제하면 사용자 메뉴로 복귀
		
	}
	
	public void deletePhoneNumFromDB(String name) {
		
		Connection conn = null;
//		INSERT 등의 데이터 조작 쿼리를 실행시켜주는 변수
		PreparedStatement pstmt = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";			
			conn = DriverManager.getConnection(url, "delivery", "dbpassword");
		
//			INSERT 쿼리를 먼저 작성!
			String deleteSql = String.format("DELETE FROM phone_numbers "
					+ "WHERE name = '%s' AND user_id = %d;", name, GlobalData.loginUser.getId());
			
			
//			DB연결이 되었으니, INSERT 쿼리를 날릴 준비.						
			pstmt = conn.prepareStatement(deleteSql);
			
//			INSERT/UPDATE/DELET 문을 실행하면, 영향 받은 줄이 몇줄인지?
			int affectedRowCount = pstmt.executeUpdate();
			
			if(affectedRowCount == 0) {
				System.out.println("전화번호를 삭제 하지 못했습니다. 입력한 이름을 확인해 주세요.");
			}
			else {
				System.out.println(String.format("%d개의 전화번호가 삭제되었습니다.", affectedRowCount));
				System.out.println("사용자 메뉴로 돌아갑니다.");
			}
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}























