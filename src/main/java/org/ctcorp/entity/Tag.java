package org.ctcorp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

//@Entity
//@Table(name = "table_tag")
public class Tag {
	private long id;
	private String label;
//	private List<Post> posts = new ArrayList<Post>();
	
//	@Id
//	@GeneratedValue
//	@Column(name = "id")
//	public long getId() {
//		return id;
//	}
//	public void setId(long id) {
//		this.id = id;
//	}
//	
//	@Column(name = "label")
//	public String getLabel() {
//		return label;
//	}
//	public void setLabel(String label) {
//		this.label = label;
//	}
//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JsonBackReference
//	@JoinTable(name = "table_post_tag", joinColumns = @JoinColumn(name = "id_tag", referencedColumnName = "id_tag"),
//    inverseJoinColumns = @JoinColumn(name = "id_post", referencedColumnName = "id_post"))
//	public List<Post> getPosts() {
//		return posts;
//	}
//	public void setPosts(List<Post> posts) {
//		this.posts = posts;
//	}
}
