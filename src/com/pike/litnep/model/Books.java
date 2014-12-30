package com.pike.litnep.model;

public class Books {
	private int bookId;
	private String coverUrl;
	private String title;
	private String author;
	private String year;
	private String review;

	public Books() {
	}

	public Books(String coverUrl, String title, String author, String year,
			String review) {
		this.coverUrl = coverUrl;
		this.title = title;
		this.author = author;
		this.year = year;
		this.review = review;
	}

	/**
	 * Setters
	 */
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setReview(String review) {
		this.review = review;
	}

	/**
	 * Getters
	 */
	public int getBookId() {
		return bookId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getYear() {
		return year;
	}

	public String getReview() {
		return review;
	}
}
