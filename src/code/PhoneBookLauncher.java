package code;

import java.util.Scanner;

public class PhoneBookLauncher {
	
	public void startPhoneBook() {
		
		System.out.println("전화번호부를 시작합니다.");
		
		Scanner scan = new Scanner(System.in);
		
//		0번을 누를때까지 무한반복
		
		while (true) {
			
			printMainMenu();
//			메뉴를 출력한 뒤 숫자를 입력받는다
			int userInput = scan.nextInt();	
			if (userInput == 1) {
//				로그인	
			}
			else if (userInput ==2) {
//				회원가입
			}
			else if (userInput ==0) {
				System.out.println("프로그램 종료");
				break;
			}
			else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요");
				
			}
		}
		
		printMainMenu();
	}

//	메인화면을 출력하는 메쏘드
	public void printMainMenu() {
		System.out.println("********** 전화번호부 **********");
		System.out.println(" 1.로그인");
		System.out.println(" 2. 회원가입");
		System.out.println(" 0. 프로그램 종료");
		System.out.println("****************************");
		System.out.println("원하는 동작을 입력 : ");
		
	}
}
