package re1kur.rentalservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.dto.car.CarWriteDto;
import re1kur.rentalservice.dto.car.details.CarDetailsWriteDto;
import re1kur.rentalservice.dto.car.filter.CarFilter;
import re1kur.rentalservice.dto.car.images.CarImageWriteDto;
import re1kur.rentalservice.service.CarService;
import re1kur.rentalservice.service.MakeService;

import java.io.IOException;

@Controller
@RequestMapping("cars")
public class CarsController {
    private final CarService service;
    private final MakeService makeService;
    @Value("${custom.pagination.size}")
    private Integer size;

    @Autowired
    public CarsController(CarService service, MakeService makeService) {
        this.service = service;
        this.makeService = makeService;
    }

    @GetMapping("list")
    public String listCars(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @ModelAttribute(name="filter")CarFilter filter) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarReadDto> carPage = service.readAll(filter, pageable);

        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("currentPage", carPage.getNumber());
        model.addAttribute("totalPages", carPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", carPage.getTotalElements());
        model.addAttribute("filter", filter);

        model.addAttribute("prevPage", carPage.hasPrevious() ? carPage.getNumber() - 1 : null);
        model.addAttribute("nextPage", carPage.hasNext() ? carPage.getNumber() + 1 : null);
        model.addAttribute("lastPage", carPage.getTotalPages() - 1);

        return "cars/cars-list.html";
    }

    @GetMapping("/create")
    public String getCreateCar(Model model) {
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("write", new CarWriteDto());
        model.addAttribute("carDetails", new CarDetailsWriteDto());
        return "cars/car-create.html";
    }

    @Transactional
    @PostMapping("/create")
    public String createCar(
            @Validated @ModelAttribute("write") CarWriteDto car,
            @Validated @ModelAttribute("carDetails") CarDetailsWriteDto carDetails,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") MultipartFile title
    ) throws IOException {
        car.setDetails(carDetails);
        car.setTitleImage(CarImageWriteDto.builder().image(title).build());
        car.setImage(CarImageWriteDto.builder().image(file).build());
        Integer id = service.writeCar(car);
        return "redirect:/cars/" + id;
    }

}
