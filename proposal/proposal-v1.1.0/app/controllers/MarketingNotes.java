package controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketingNotes {
private String  noteName;
private String  noteContent;
public String getNoteName() {
	return noteName;
}
public void setNoteName(String noteName) {
	this.noteName = noteName;
}
public String getNoteContent() {
	return noteContent;
}
public void setNoteContent(String noteContent) {
	this.noteContent = noteContent;
}


}

