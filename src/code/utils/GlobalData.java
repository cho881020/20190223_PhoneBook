package code.utils;

import java.util.ArrayList;

import code.datas.User;

// 이 클래스에 만드는 변수는 모든 클래스에서 쉽게 접근하기 위한 목적

public class GlobalData {
	
//	로그인한 사람의 정보를 담아두는 변수.
//	null이라면 로그인한 사람이 없다!
//	null 아니면 로그인한사람 정보 접근 가능.
	public static User loginUser = null;
	
//	로그인한 사람이 저장한 폰번들이 담겨있는 ArrayList
	
	public static List<PhoneNumber> loginUserPhoneNums = null;

}
