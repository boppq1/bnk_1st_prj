package com.example.demo.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.http.HttpSession;

import java.util.List;


@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SearchController {
	
	private final SearchService searchService;
	private final JwtUtil jwt;
	
    @GetMapping("/suggested")
    public ResponseEntity<List<SearchDto>> getSuggested(
            @RequestParam(value = "q", required = false) String query) {
    	
        List<SearchDto> result = (query == null || query.isBlank())
                ? searchService.findAllOrderByVolume()
                : searchService.findByKeywordChars(query.trim());
 
        return ResponseEntity.ok(result);
    }

}
