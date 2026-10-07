package com.bhavesh.orbit.connectionService.repository;

import com.bhavesh.orbit.connectionService.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByUserId(Long userId);

    @Query("""
    MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person)
    WHERE personA.userId = $userId
    RETURN personB
    """)
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("""
    MATCH (personA:Person)-[:CONNECTED_TO*2]-(personB:Person)
    WHERE personA.userId = $userId
      AND personB.userId <> $userId
    RETURN DISTINCT personB
    """)
    List<Person> getSecondDegreeConnections(Long userId);

    @Query("""
    MATCH (personA:Person)-[:CONNECTED_TO*3]-(personB:Person)
    WHERE personA.userId = $userId
      AND personB.userId <> $userId
    RETURN DISTINCT personB
    """)
    List<Person> getThirdDegreeConnections(Long userId);
}
