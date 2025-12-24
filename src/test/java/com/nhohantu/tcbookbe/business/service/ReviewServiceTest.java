package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.ReviewRequest;
import com.nhohantu.tcbookbe.business.dto.response.ReviewResponse;
import com.nhohantu.tcbookbe.business.repository.IOrderRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.business.repository.IReviewRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private IReviewRepository reviewRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private UserBasicInfoService userBasicInfoService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewService reviewService;

    private UserBasicInfoModel currentUser;
    private OrderModel order;
    private ProductModel product;
    private ReviewRequest validRequest;

    @BeforeEach
    void setUp() {
        // Setup current user
        currentUser = new UserBasicInfoModel();
        currentUser.setId(1L);
        currentUser.setUsername("testuser");

        // Setup product
        product = new ProductModel();
        product.setId(1L);
        product.setName("Test Product");

        // Setup order detail
        OrderDetailModel orderDetail = new OrderDetailModel();
        orderDetail.setProduct(product);

        // Setup order
        order = new OrderModel();
        order.setId(1L);
        order.setUser(currentUser);
        order.setStatus(OrderStatus.DELIVERED);
        order.setOrderDetails(List.of(orderDetail));

        // Setup valid request
        validRequest = ReviewRequest.builder()
                .orderId(1L)
                .productId(1L)
                .name("Test User")
                .email("test@example.com")
                .message("Sản phẩm rất tốt, giao hàng nhanh chóng")
                .rating(5)
                .build();
    }

    @Test
    @DisplayName("1. Auth Check - Không đăng nhập thì không được review")
    void createReview_WhenNotLoggedIn_ShouldReturnError() {
        // Given
        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(null);

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Vui lòng đăng nhập để đánh giá sản phẩm", response.getBody().getMessage());
        verify(orderRepository, never()).findById(anyLong());
    }


    @Test
    @DisplayName("2. Order Not Found - Đơn hàng không tồn tại")
    void createReview_WhenOrderNotFound_ShouldReturnError() {
        // Given
        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Không tìm thấy đơn hàng"));
    }

    @Test
    @DisplayName("3. Ownership Check - Đơn hàng không phải của user đang login")
    void createReview_WhenOrderNotOwnedByUser_ShouldReturnError() {
        // Given
        UserBasicInfoModel anotherUser = new UserBasicInfoModel();
        anotherUser.setId(999L);
        order.setUser(anotherUser);

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Bạn không có quyền đánh giá đơn hàng này", response.getBody().getMessage());
    }

    @Test
    @DisplayName("4. Status Check - Đơn hàng chưa DELIVERED (PENDING)")
    void createReview_WhenOrderNotDelivered_Pending_ShouldReturnError() {
        // Given
        order.setStatus(OrderStatus.PENDING);

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("DELIVERED"));
    }

    @Test
    @DisplayName("4b. Status Check - Đơn hàng đã CANCELLED")
    void createReview_WhenOrderCancelled_ShouldReturnError() {
        // Given
        order.setStatus(OrderStatus.CANCELLED);

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("DELIVERED"));
    }

    @Test
    @DisplayName("5. Product Not Found - Sản phẩm không tồn tại")
    void createReview_WhenProductNotFound_ShouldReturnError() {
        // Given
        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Không tìm thấy sản phẩm"));
    }

    @Test
    @DisplayName("6. Product Not In Order - Sản phẩm không có trong đơn hàng")
    void createReview_WhenProductNotInOrder_ShouldReturnError() {
        // Given
        ProductModel anotherProduct = new ProductModel();
        anotherProduct.setId(999L);
        anotherProduct.setName("Another Product");

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(999L)).thenReturn(Optional.of(anotherProduct));

        ReviewRequest requestWithDifferentProduct = ReviewRequest.builder()
                .orderId(1L)
                .productId(999L)
                .name("Test User")
                .email("test@example.com")
                .message("Sản phẩm rất tốt, giao hàng nhanh chóng")
                .rating(5)
                .build();

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(requestWithDifferentProduct);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Sản phẩm này không có trong đơn hàng của bạn", response.getBody().getMessage());
    }


    @Test
    @DisplayName("7. Duplicate Check - Đã review sản phẩm này trong đơn hàng này rồi")
    void createReview_WhenAlreadyReviewed_ShouldReturnError() {
        // Given
        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByOrderIdAndProductId(1L, 1L)).thenReturn(true);

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Bạn đã đánh giá sản phẩm này trong đơn hàng này rồi", response.getBody().getMessage());
        verify(reviewRepository, never()).save(any(ReviewModel.class));
    }

    @Test
    @DisplayName("8. Success - Tạo review thành công khi tất cả điều kiện hợp lệ")
    void createReview_WhenAllConditionsValid_ShouldCreateReviewSuccessfully() {
        // Given
        ReviewModel savedReview = ReviewModel.builder()
                .order(order)
                .product(product)
                .authorName("Test User")
                .authorEmail("test@example.com")
                .rating(5)
                .title("Sản phẩm rất tốt, giao hàng nhanh chóng")
                .description("Sản phẩm rất tốt, giao hàng nhanh chóng")
                .build();

        ReviewResponse expectedResponse = ReviewResponse.builder()
                .id(1L)
                .orderId(1L)
                .productId(1L)
                .productName("Test Product")
                .authorName("Test User")
                .rating(5)
                .build();

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByOrderIdAndProductId(1L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(ReviewModel.class))).thenReturn(savedReview);
        when(modelMapper.map(any(ReviewModel.class), eq(ReviewResponse.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(validRequest);

        // Then
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Tạo đánh giá thành công", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(reviewRepository, times(1)).save(any(ReviewModel.class));
    }

    @Test
    @DisplayName("9. Success - Kiểm tra title được tạo tự động từ message dài")
    void createReview_WhenMessageLong_ShouldTruncateTitle() {
        // Given
        String longMessage = "Đây là một đánh giá rất dài để kiểm tra việc tạo title tự động từ message khi message quá 50 ký tự";
        
        ReviewRequest requestWithLongMessage = ReviewRequest.builder()
                .orderId(1L)
                .productId(1L)
                .name("Test User")
                .email("test@example.com")
                .message(longMessage)
                .rating(5)
                .build();

        ReviewModel savedReview = ReviewModel.builder()
                .order(order)
                .product(product)
                .authorName("Test User")
                .authorEmail("test@example.com")
                .rating(5)
                .title(longMessage.substring(0, 50) + "...")
                .description(longMessage)
                .build();

        ReviewResponse expectedResponse = new ReviewResponse();

        when(userBasicInfoService.getUserInfoFromContext()).thenReturn(currentUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByOrderIdAndProductId(1L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(ReviewModel.class))).thenReturn(savedReview);
        when(modelMapper.map(any(ReviewModel.class), eq(ReviewResponse.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ResponseDTO<ReviewResponse>> response = reviewService.createReview(requestWithLongMessage);

        // Then
        assertTrue(response.getBody().isSuccess());
        verify(reviewRepository).save(argThat(review -> 
            review.getTitle().endsWith("...") && review.getTitle().length() == 53
        ));
    }
}
