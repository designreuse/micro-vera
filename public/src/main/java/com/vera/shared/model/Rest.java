package com.vera.shared.model;


import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Rest 交互的外观模型
 * @param <T> 携带数据的类型
 *
 * @author ychost
 * @date 2018-10-05
 */
@Data
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor
@Accessors(fluent =false)
@ToString
public class Rest<T> {
   private T data;
   private int code;
   private String message;
   private Object ext;


   public Rest<T> with(int code,String message,T data){
      this.code = code;
      this.message = message;
      this.data = data;
      return this;
   }

   public Rest<T> with(int code,String message){
      return with(code,message,null);
   }

   public Rest<T> with(int code){
      return with(code,null,null);
   }

   public Rest<T> withMessages(List<?> messages){
      if(messages == null){
         return this;
      }
      StringBuilder sb = new StringBuilder();
      for (Object o : messages) {
         sb.append(o.toString());
      }
      this.message = sb.toString();
      return this;
   }
}
