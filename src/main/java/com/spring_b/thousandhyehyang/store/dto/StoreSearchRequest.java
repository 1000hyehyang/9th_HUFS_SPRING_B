package com.spring_b.thousandhyehyang.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSearchRequest {

    @Size(max = 10, message = "지역 필터는 최대 10개까지 선택할 수 있습니다")
    private List<String> regions;

    private String searchKeyword;

    @Builder.Default
    private String sortBy = "latest";

    private String sortDirection;

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
    @Builder.Default
    private Integer size = 10;
}
