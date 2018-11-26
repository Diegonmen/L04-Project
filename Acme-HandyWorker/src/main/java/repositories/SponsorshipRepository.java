package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Sponsorship;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

}
