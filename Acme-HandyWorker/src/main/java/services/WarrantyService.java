package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Warranty;
import repositories.WarrantyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class WarrantyService {

	@Autowired
	private WarrantyRepository warrantyRepository;

	public Warranty save(Warranty warranty) {
		Warranty result, saved;
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		
		if(exists(warranty.getId()) && logedUserAccount.getAuthorities().contains(authority) && !warranty.isFinalMode()) {
			saved = this.warrantyRepository.findOne(warranty.getId());
			Assert.notNull(saved);
			result = this.warrantyRepository.save(warranty);
			Assert.notNull(result);
			return result;
		}
		
		result = this.warrantyRepository.findOne(warranty.getId());
		return result;
	}

	public List<Warranty> findAll() {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return warrantyRepository.findAll();
	}

	public Warranty findOne(Integer warrantyId) {
		Assert.isTrue(exists(warrantyId));
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return warrantyRepository.findOne(warrantyId);
	}

	public boolean exists(Integer warrantyId) {
		return warrantyRepository.exists(warrantyId);
	}

	public void delete(Warranty warranty) {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority) && !warranty.isFinalMode());
		warrantyRepository.delete(warranty);
	}
	
	public Collection<Warranty> findDraftModeWarranties(){
		Collection<Warranty> res = warrantyRepository.findDraftModeWarranties();
		Assert.notEmpty(res);
		return res;
	}
}
