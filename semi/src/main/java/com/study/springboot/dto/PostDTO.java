package com.study.springboot.dto;

public class PostDTO {
		private Long id;
		private String title;
	    private String content;
	    private String userName;
	    private String userId; // ✅ 변경: Long → String

    public PostDTO() {}

    public PostDTO(Long id, String title, String content, String userName, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
