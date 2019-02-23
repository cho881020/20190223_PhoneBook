package code.utils;

import code.datas.User;

// 이 클래스에 만드는 변수는 모든 클래스에서 쉽게 접근하기 위한 목적
public class GlobalData {
//	로그인한 사람의 정보를 담아두는 변수
//	null이 아니면 로그인한 사람 정보에 접근 가능
	public static User loginUser = null;
}
