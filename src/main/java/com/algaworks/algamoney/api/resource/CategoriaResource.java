package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public  List<Categoria> listar(){

        return  categoriaRepository.findAll();
        // -Metodo errado pois fica confuso return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();

    }

    @PostMapping
   // @ResponseStatus(HttpStatus.CREATED) -> Anotação Criada caso não seja preciso retornar um ResponseEntity
    public ResponseEntity <Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response){
        Categoria categoriaSalva =  categoriaRepository.save(categoria);
        //Atraves da Classe ServletUriComponentsBuilder eu vou pegar a partir da requisição atual que foi /categorias adicionar o codigo
        //e adicionar esse codigo na Uri, setar o header com está uri
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo()).toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(categoriaSalva); //Mostrando o Json de saida no Bodystatus
    }

    @GetMapping("/{codigo}")
    public Categoria buscarPeloCodigo(@PathVariable Long codigo){
        return categoriaRepository.findOne(codigo);
    }

}
