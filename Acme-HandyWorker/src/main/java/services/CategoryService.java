package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Category;
import repositories.CategoryRepository;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category save(Category entity) {
		return categoryRepository.save(entity);
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findOne(Integer id) {
		return categoryRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return categoryRepository.exists(id);
	}

	public void delete(Category entity) {
		categoryRepository.delete(entity);
	}

	
	
	
	
}
