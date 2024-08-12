package ru.marat.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marat.springcourse.dao.PersonDAO;
import ru.marat.springcourse.models.Person;
import ru.marat.springcourse.services.PeopleService;
import ru.marat.springcourse.util.PersonValidator;

import javax.validation.Valid;
import java.sql.SQLException;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    public PeopleController(PersonDAO personDAO, PersonValidator personValidator, PeopleService peopleService) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping() // Адрес - /people
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll()); // Под ключ people кладём список людей
        return "people/index";
    }

    @GetMapping("/{id}") // Во время выполнения сюда можно поместить любое значение и оно будет записано в @PathVariable
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) { // Аннотация @ModelAttribute создаст новый объект класса Person и положит в него данные из формы

        personValidator.validate(person, bindingResult); // Кладём ошибки в bindingResult из валидатора

        if(bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person); // @Valid нужна для валидации значений с формы. Если значения нарушаются, то ошибка помещается в bindingResult
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) throws SQLException {
        model.addAttribute("person", peopleService.findOne(id)); // Добавляем в модель человека, которого получаем по id для его дальнейшего редактирования
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) throws SQLException {

        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()) {
            return "people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

}
