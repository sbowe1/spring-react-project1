package com.example.p1_backend.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.p1_backend.models.Resource;
import com.example.p1_backend.models.Subtopic;
import com.example.p1_backend.models.Topic;
import com.example.p1_backend.models.dtos.InResourceDto;
import com.example.p1_backend.models.dtos.OutResourceDto;
import com.example.p1_backend.repositories.ResourceDao;
import com.example.p1_backend.repositories.SubtopicDao;
import com.example.p1_backend.repositories.TopicDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResourceService {

	private ResourceDao resourceDao;

	private TopicDao topicDao;

	private SubtopicDao subtopicDao;

	@Autowired
	public ResourceService(ResourceDao resourceDao, TopicDao topicDao, SubtopicDao subtopicDao) {
		this.resourceDao = resourceDao;
		this.topicDao = topicDao;
		this.subtopicDao = subtopicDao;
	}

	/**
	 * Creates a new resource with no subtopic.
	 * @param	topicId
	 * @param	resourceDto
	 * @return	OutResourceDto
	 */
	public OutResourceDto createResourceNoSubtopic(int topicId, InResourceDto resourceDto) {
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		Resource resource = new Resource();
		resource.setTitle(resourceDto.getTitle());
		resource.setDescription(resourceDto.getDescription());
		resource.setType(resourceDto.getType());
		resource.setTopic(optTopic.get());
		if (resourceDto.getUrl() != null && !resourceDto.getDescription().isEmpty()) {
			resource.setUrl(resourceDto.getUrl());
		}

		resource = resourceDao.save(resource);
		log.info("Resource: {} created successfully", resource.getTitle());

		return new OutResourceDto(resource.getResourceId(), resource.getTitle(), resource.getDescription(),
				resource.getType(), resource.getUrl(), resource.getTopic().getTitle(), null);
	}

	/**
	 * Creates a new resource with a subtopic.
	 * @param	topicId
	 * @param	subtopicId
	 * @param	resourceDto
	 * @return	OutResourceDto
	 */
	public OutResourceDto createResourceSubtopic(int topicId, int subtopicId, InResourceDto resourceDto) {
		Optional<Topic> optTopic = topicDao.findById(topicId);
		if (optTopic.isEmpty()) {
			log.warn("Topic does not exist");
			throw new NoSuchElementException("Topic does not exist");
		}

		Optional<Subtopic> optSubtopic = subtopicDao.findById(subtopicId);
		if (optSubtopic.isEmpty()) {
			log.warn("Subtopic does not exist");
			throw new NoSuchElementException("Subtopic does not exist");
		}

		Resource resource = new Resource();
		resource.setTitle(resourceDto.getTitle());
		resource.setDescription(resourceDto.getDescription());
		resource.setType(resourceDto.getType());
		if (resourceDto.getUrl() != null && !resourceDto.getUrl().isEmpty()) {
			resource.setUrl(resourceDto.getUrl());
		}
		resource.setSubtopic(optSubtopic.get());
		resource.setTopic(optTopic.get());

		resource = resourceDao.save(resource);
		log.info("Resource: {} created successfully", resource.getTitle());

		return new OutResourceDto(resource.getResourceId(), resource.getTitle(), resource.getDescription(),
				resource.getType(), resource.getUrl(), resource.getTopic().getTitle(),
				resource.getSubtopic().getTitle());
	}

	/**
	 * Reads a resource by id.
	 * @param	resourceId
	 * @return	OutResourceDto
	 */
	public OutResourceDto readResource(int resourceId) {
		Optional<Resource> optResource = resourceDao.findById(resourceId);
		if (optResource.isEmpty()) {
			log.warn("Resource does not exist");
			throw new NoSuchElementException("Resource does not exist");
		}

		OutResourceDto outResourceDto = new OutResourceDto();
		outResourceDto.setResourceId(optResource.get().getResourceId());
		outResourceDto.setTitle(optResource.get().getTitle());
		outResourceDto.setDescription(optResource.get().getDescription());
		outResourceDto.setType(optResource.get().getType());
		outResourceDto.setUrl(optResource.get().getUrl());
		outResourceDto.setTopicName(optResource.get().getTopic().getTitle());
		if (optResource.get().getSubtopic() != null) {
			outResourceDto.setSubtopicName(optResource.get().getSubtopic().getTitle());
		}
		return outResourceDto;
	}

	/**
	 * Updates a resource.
	 * @param	resourceId
	 * @param	resourceDto
	 * @return	OutResourceDto
	 */
	public OutResourceDto updateResource(int resourceId, InResourceDto resourceDto) {
		Optional<Resource> optResource = resourceDao.findById(resourceId);
		if (optResource.isEmpty()) {
			log.warn("Resource does not exist");
			throw new NoSuchElementException("Resource does not exist");
		}

		if (resourceDto.getTitle() != null && !resourceDto.getTitle().isEmpty()) {
			optResource.get().setTitle(resourceDto.getTitle());
		}
		if (resourceDto.getDescription() != null && !resourceDto.getDescription().isEmpty()) {
			optResource.get().setDescription(resourceDto.getDescription());
		}
		if (resourceDto.getType() != null && !resourceDto.getType().isEmpty()) {
			optResource.get().setType(resourceDto.getType());
		}
		if (resourceDto.getUrl() != null && !resourceDto.getUrl().isEmpty()) {
			optResource.get().setUrl(resourceDto.getUrl());
		}

		resourceDao.save(optResource.get());
		log.info("Resource: {} updated successfully", optResource.get().getTitle());

		OutResourceDto outResourceDto = new OutResourceDto();
		outResourceDto.setResourceId(optResource.get().getResourceId());
		outResourceDto.setTitle(optResource.get().getTitle());
		outResourceDto.setDescription(optResource.get().getDescription());
		outResourceDto.setType(optResource.get().getType());
		outResourceDto.setUrl(optResource.get().getUrl());
		outResourceDto.setTopicName(optResource.get().getTopic().getTitle());
		if (optResource.get().getSubtopic() != null) {
			outResourceDto.setSubtopicName(optResource.get().getSubtopic().getTitle());
		}
		return outResourceDto;
	}

}
