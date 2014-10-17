package edu.purdue.cs.baseline.tweetspair;

public class Tweet {
	private Integer id;
	private Double x;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Tweet(Integer id, Double x, Double y, String text, Long time) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.text = text;
		this.time = time;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	private Double y;
	private String text;
	private Long time;
}
