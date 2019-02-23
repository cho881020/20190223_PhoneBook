package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;

public class PhoneBookLauncher {

	public void startPhoneBook() {
		
		System.out.println("전화번호부를 시작합니다.");
		
		Scanner scan = new Scanner(System.in);
		
//		0번을 누를때까지 무한 반복.
		while(true) {
			printMainMenu();
			
			int userInput = scan.nextInt();
			
			if(userInput == 1) {
//				로그인
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
			else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
		}
	}
	
	public void printMainMenu() {
		
		System.out.println("********** 전화번호 부 **********");
		System.out.println(" 1. 로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 0. 프로그램 종료");
		System.out.println("******************************");
		System.out.println("원하는 동작을 입력 : ");
		
	}
	
//	아이디, 비번을 입력받아서 DB에 로그인
	public void login() {
		
//		아이디를 입력받아서 저장하는 기능이다.
		Scanner scan1 = new Scanner(System.in);
		System.out.print("ID(Email) : ");
		String userInputEmail = scan1.nextLine();
		
//		비밀번호를 입력받아서 저장하는 기능
		Scanner scan2 = new Scanner(System.in);
		System.out.print("pw : ");
		String userInputPassword = scan2.nextLine();
		
//		JDBC를 통해서 db서버에 실제 사용자가 맞는지 문의. Query
		boolean loginResult = findUserDatabase(userInputEmail, userInputEmail);
		if(loginResult) {
//			로그인 성공! 성공 처리 메쏘드 별개 처리
		}
		else{
			System.out.println("메인화면으로 이동합니다.");
		}
		
	}
	
//	아이디와 비밀번호를 가지고 실제로 DB에 있는지 쿼리를 날리 메쏘드.
//	있으면 True, 없으면 False를 리턴
	
	public boolean findUserDatabase(String email, String pw) {
		boolean result = false;
		
//		DB와 연결 상태를 저장해두는 변수
		Connection conn = null;
//		연결된 DB에 쿼리를 실행 시켜줌.
		Statement stmt = null;
//		쿼리 실행 결과를 저장하는 변수 (표).
		Resultset rs = null;
		
//		드라이버 로드
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
//			불러온 jdbc를 이용해 접속.
//			접속 정보를 변수에 저장
			String url = "jdbc:mysql://delivery.c0ctoatt9tr3.ap-northeast-2.rds.amazonaws.com/tjeit";
			
//			저장된 접속 정보와 아이디 비번을 가지고 실제로 db에 접속.
			conn = DriverManager.getConnection(url, "delivery", "dbpassword");
			
			System.out.println("DB 연결 성공!");
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
}


























