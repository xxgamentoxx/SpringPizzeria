package com.example.demo.domain.imageDomain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.core.EntityBase;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash("Image")
public @NoArgsConstructor @Getter @Setter class ImageEntity extends EntityBase{

    @NotNull @Size(min=1)
    private byte[] data;

}