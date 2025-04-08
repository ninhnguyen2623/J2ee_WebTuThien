package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Blogs.BlogCreateDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogResponseDto;
import com.example.springdonateweb.Models.Dtos.Blogs.BlogUpdateDto;
import com.example.springdonateweb.Models.Entities.BlogEntity;
import com.example.springdonateweb.Repositories.BlogRepository;
import com.example.springdonateweb.Services.interfaces.IBlogService;
import com.example.springdonateweb.Services.mappers.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService implements IBlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public Page<BlogResponseDto> findBlogsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogEntity> blogEntities = blogRepository.findAll(pageable);
        return blogEntities.map(blogMapper::toDto);  // Map BlogEntity to BlogResponseDto
    }
    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        // Kiểm tra xem tệp có trống không
        if (image.isEmpty()) {
            throw new IOException("No image file provided.");
        }

        // Đặt đường dẫn lưu tệp hình ảnh
        String fileName = image.getOriginalFilename();
        String uploadDir = "D:/Git/SpringDonateWeb/src/main/resources/static/uploads/images/"; // Cập nhật đường dẫn cho đúng
        File uploadDirPath = new File(uploadDir);

        // Kiểm tra xem thư mục đã tồn tại chưa, nếu không thì tạo
        if (!uploadDirPath.exists()) {
            uploadDirPath.mkdirs();
        }

        String imagePath = uploadDir + fileName;
        File file = new File(imagePath);

        // Lưu tệp vào thư mục
        image.transferTo(file);
        return "uploads/images/" + fileName;  // Trả về URL hình ảnh đã lưu
    }

    @Override
    public BlogResponseDto findById(int id) {
        Optional<BlogEntity> blogEntity = blogRepository.findById(id);
        return blogEntity.map(blogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    @Override
    public BlogResponseDto create(BlogCreateDto blogCreateDto) {
        BlogEntity blogEntity = blogMapper.toEntity(blogCreateDto);
        BlogEntity savedBlog = blogRepository.save(blogEntity);
        return blogMapper.toDto(savedBlog);
    }

    @Override
    public BlogResponseDto update(BlogUpdateDto blogUpdateDto) {
        BlogEntity blogEntity = blogRepository.findById(blogUpdateDto.getId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        blogEntity = blogMapper.partialUpdate(blogUpdateDto, blogEntity);
        blogRepository.save(blogEntity);
        return blogMapper.toDto(blogEntity);
    }

    @Override
    public void delete(int id) {
        blogRepository.deleteById(id);
    }
    @Override
    public List<BlogResponseDto> findAll() {
        return blogRepository.findAll().stream()
                .map(blogMapper::toDto)
                .toList();
    }
}
