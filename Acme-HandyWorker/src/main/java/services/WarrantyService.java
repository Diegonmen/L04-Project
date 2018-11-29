package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Warranty;
import repositories.WarrantyRepository;

@Service
@Transactional
public class WarrantyService {

	@Autowired
	private WarrantyRepository warrantyRepository;

	
	
	public List<Warranty> findAll() {
		return warrantyRepository.findAll();
	}



	public Warranty findOne(Integer id) {
		return warrantyRepository.findOne(id);
	}



	public Warranty save(Warranty entity) {
		return warrantyRepository.save(entity);
	}



	public boolean exists(Integer id) {
		return warrantyRepository.exists(id);
	}



	public void delete(Warranty entity) {
		warrantyRepository.delete(entity);
	}



	public Collection<Warranty> findDraftModeWarranties(){
		Collection<Warranty> res = warrantyRepository.findDraftModeWarranties();
		Assert.notEmpty(res);
		return res;
	}
}
