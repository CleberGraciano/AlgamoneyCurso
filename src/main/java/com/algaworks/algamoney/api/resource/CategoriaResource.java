package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping
    public List<Categoria> listar() {

        return categoriaRepository.findAll();
        // Metodo errado pois fica confuso return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();

    }

    @PostMapping
    // @ResponseStatus(HttpStatus.CREATED) -> Anotação Criada caso não seja preciso retornar um ResponseEntity
    // @Valid é usado para validar se ao mandar algo para o banco ele possui uma anotação notnull no campo valida campos vazios para isso precisa ter a anotação no parametro no model @NotNull
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {

        Categoria categoriaSalva = categoriaRepository.save(categoria);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva); //Mostrando o Json de saida no Bodystatus
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
        Categoria cat = categoriaRepository.findOne(codigo);
        //Responsavél por verivicar se ao buscar um registro ele existe, e retorna o registro, caso contrario muda o codigo de resposta para 404 que não existe
        return cat != null ? ResponseEntity.ok(cat) : ResponseEntity.notFound().build();
    }
}