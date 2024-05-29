package br.com.vcoroa.accountmanager.repositories;

import br.com.vcoroa.accountmanager.entities.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {
   Optional<Usuario> findByUsername(String username);
}