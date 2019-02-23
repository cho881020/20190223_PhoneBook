package code;

import java.sql.Connection;
import java.sql.DriverManager;
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
		
		System.out.println("********** 전화번호부 **********");
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
	
}























