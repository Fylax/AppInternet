package it.polito.ai.springserver.resource.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.List;

public class PaginationSupportClass {

  private List<Resource> items;
  private int totalElements;
  private List<Link> links;

  public PaginationSupportClass(List<Resource> items, int totalElements, List<Link> links) {
    this.items = items;
    this.totalElements = totalElements;
    this.links = links;
  }

  public PaginationSupportClass() {

  }

  public List<Resource> getItems() {
    return items;
  }

  public void setItems(List<Resource> items) {
    this.items = items;
  }

  public int getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(int totalElements) {
    this.totalElements = totalElements;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }
}
