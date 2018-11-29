package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Category;
import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category save(Category category) {
		Category result, saved;
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		
		if(exists(category.getId()) && logedUserAccount.getAuthorities().contains(authority)) {
			saved = this.categoryRepository.findOne(category.getId());
			Assert.notNull(saved);
			result = this.categoryRepository.save(category);
			Assert.notNull(result);
			return result;
		}
		
		result = this.categoryRepository.findOne(category.getId());
		return result;
	}

	public List<Category> findAll() {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return categoryRepository.findAll();
	}

	public Category findOne(Integer categoryId) {
		Assert.isTrue(exists(categoryId));
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return categoryRepository.findOne(categoryId);
	}

	public boolean exists(Integer categoryId) {
		return categoryRepository.exists(categoryId);
	}

	public void delete(Category category) {
		Assert.notNull(category);
		Assert.isTrue(exists(category.getId()));
		
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		categoryRepository.delete(category);
	}

	
	
	
}
