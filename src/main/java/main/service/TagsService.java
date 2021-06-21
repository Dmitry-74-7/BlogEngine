package main.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import main.api.response.tagResponse.TagsResponse;
import main.api.response.tagResponse.TagsStruct;
import main.model.repositories.PostsRepository;
import main.model.repositories.TagsRepository;
import main.model.tables.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagsService extends PostService{

  private final TagsRepository tagsRepository;
  private final PostsRepository postsRepository;

  @Autowired
  public TagsService(TagsRepository tagsRepository,
      PostsRepository postsRepository) {
    super(postsRepository);
    this.tagsRepository = tagsRepository;
    this.postsRepository = postsRepository;
  }

  public TagsResponse tagsResponse (String query) {
    TagsResponse tagsResponse = new TagsResponse();
    tagsResponse.setTags(tagsStructCollection(query));
    return tagsResponse;
  }

  private Collection<TagsStruct> tagsStructCollection (String query) {
    List<TagsStruct> tagsList = new ArrayList<>();
    int activePostsCount = postsRepository.countPosts();
    Integer maxPostCount = tagsRepository.maxPostCount();
    if (activePostsCount == 0 || maxPostCount == null || maxPostCount == 0) {
      return tagsList;
    }
    double k = 1 /((double) maxPostCount / (double) activePostsCount);
    List<Tags> tags;
    tags = tagsRepository.tagsListQuery(query);
    if (tags.size() == 0) {
       tags = tagsRepository.findAll();;
       if (tags.size() == 0) {
         return tagsList;
       }
    }
    for (Tags tag: tags) {
      TagsStruct tagsStruct = new TagsStruct();
      tagsStruct.setName(tag.getName());
      tagsStruct.setWeight((double) tag.getPosts().size() / (double) activePostsCount * k);
      tagsList.add(tagsStruct);
    }
    return tagsList;
  }
}
