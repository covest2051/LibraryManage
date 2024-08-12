package ru.marat.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marat.springcourse.models.Book;
import ru.marat.springcourse.models.Person;
import ru.marat.springcourse.services.BookService;
import ru.marat.springcourse.services.PeopleService;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;
    private final PeopleService peopleService;


    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping() // Адрес - /books
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if(page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAll(sortByYear));
        else
            model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int book_id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(book_id));

        Person bookOwner = bookService.getBookOwner(book_id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) { // Аннотация @ModelAttribute создаст новый объект класса Person и положит в него данные из формы

        if(bindingResult.hasErrors())
            return "books/new";

        bookService.save(book); // @Valid нужна для валидации значений с формы. Если значения нарушаются, то ошибка помещается в bindingResult
        return "redirect:/books";
    }

    @GetMapping("/{book_id}/edit")
    public String edit(Model model, @PathVariable("book_id") int book_id) {
        model.addAttribute("book", bookService.findOne(book_id)); // Добавляем в модель человека, которого получаем по id для его дальнейшего редактирования
        return "books/edit";
    }

    @PatchMapping("/{book_id}")
    public String update(@ModelAttribute("book") @Valid Book book,BindingResult bindingResult, @PathVariable("book_id") int book_id) {

        if(bindingResult.hasErrors())
            return "books/edit";

        bookService.update(book_id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int book_id) {
        bookService.delete(book_id);
        return "redirect:/books";
    }

    @RequestMapping("/{book_id}/release")
    public String release(@PathVariable("book_id") int book_id) {
        bookService.release(book_id);
        return "redirect:/books/" + book_id;
    }

    @PostMapping("/{book_id}/assign")
    public String assign(@PathVariable("book_id") int book_id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(book_id, selectedPerson);
        return "redirect:/books/" + book_id;
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query")String query) {
        model.addAttribute("books", bookService.searchByName(query));
        return "books/search";
    }

}
