package com.summer.javacode;

public class StudyEnum {
	public static void main(String[] args) {

		// values方法获取值
		Season[] seasons = Season.values();
		System.out.println("values方法获取值：");
		for (int i = 0, inv = seasons.length; i < inv; i++) {
			System.out.println("|-->" + seasons[i]);
		}

		// valueOf方法获取值
		System.out.println("valueOf方法获取值：" + Season.valueOf("SPRING"));

		// 获取接口实现的方法
		Season.valueOf("SUMMER").show();

		// 获取枚举不存在的常量值
		try {
			System.out.println(Season.valueOf("不存在的值"));
		} catch (java.lang.IllegalArgumentException e) {
			System.out.println("没有定义的值所以抛出 java.lang.IllegalArgumentException 异常");
		}

	}
}

/**
 * 定义内部类枚举
 * @author shuang
 */
enum Season implements IShowInfo {
	SPRING("spring", "春暖花开") {
		// 实现IShowInfo接口
		public void show() {
			System.out.println("春-->有百花");
		}
	},
	SUMMER("summer", "夏日炎炎") {
		public void show() {
			System.out.println("夏-->有凉风");
		}
	},
	AUTUMN("autumn", "秋高气爽") {
		public void show() {
			System.out.println("秋-->有月");
		}
	},
	winter("winter", "银装素裹") {
		public void show() {
			System.out.println("冬-->有雪");
		}
	};

	private final String seasonName;
	private final String seasonDesc;

	private Season(String seasonName, String seasonDesc) {
		this.seasonName = seasonName;
		this.seasonDesc = seasonDesc;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public String getSeasonDesc() {
		return seasonDesc;
	}

	/**
	 * 枚举默认toString方法返回的是 the name of enum constant（枚举常量值的名字），要想返回自己定义的枚举值得重写toString方法
	 */
	@Override
	public String toString() {
		return seasonName + "," + seasonDesc;
	}
}

/**
 * 定义一个接口，让枚举类实现这个接口
 * @author shuang
 */
interface IShowInfo {
	void show();
}
