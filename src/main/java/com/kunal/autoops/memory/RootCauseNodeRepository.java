package com.kunal.autoops.memory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RootCauseNodeRepository extends JpaRepository<RootCauseNode, Long> {
}
