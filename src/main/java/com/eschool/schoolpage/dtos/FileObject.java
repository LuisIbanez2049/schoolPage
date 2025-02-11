package com.eschool.schoolpage.dtos;

public class FileObject {
    private String title;
    private String link;
    private String typeFile;
    private String fileLogo;

    public FileObject(String title, String link, String typeFile, String fileLogo) {
        this.title = title;
        this.link = link;
        this.typeFile = typeFile;
        this.fileLogo = fileLogo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public String getFileLogo() {
        return fileLogo;
    }

    public void setFileLogo(String fileLogo) {
        this.fileLogo = fileLogo;
    }
}
