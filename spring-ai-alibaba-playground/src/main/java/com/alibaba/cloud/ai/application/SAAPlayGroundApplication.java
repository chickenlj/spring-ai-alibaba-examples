/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.ai.application;

import com.alibaba.cloud.ai.vectorstore.analyticdb.AnalyticDbVectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@SpringBootApplication
public class SAAPlayGroundApplication {

	private static final Logger logger = LoggerFactory.getLogger(SAAPlayGroundApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(SAAPlayGroundApplication.class, args);
		logger.info(
				"SAAPlayGroundApplication started successfully. port: {}, swagger: {}, frontend: {}",
				8080,
				"http://localhost:8080/doc.html",
				"http://localhost:8080"
		);
	}

	@Bean
	CommandLineRunner ingestTermOfServiceToVectorStore(VectorStore vectorStore,
			@Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {

		return args -> {
			// Ingest the document into the vector store
			/*
			 * 1、文档读取TextReader 读取 resources/rag/terms-of-service.txt 文件内容
			 * 2、TokenTextSplitter 按token长度切分文本（避免大文本超出模型限制）
			 * 3、向量化存储 通过 VectorStore.write() 将文本向量存入内存（后续可用于RAG检索）
			 */
			vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));

			// 相似性搜索检测
			vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
				logger.info("Similar Document: {}", doc.getText());
			});
		};
	}

	/**
	 * 提供基于内存的向量存储（SimpleVectorStore）
	 * <p>
	 * 依赖 EmbeddingModel（自动注入，Alibaba的嵌入模型）
	 * @param embeddingModel
	 * @return
	 */
//	@Bean
//	public VectorStore vectorStore(EmbeddingModel embeddingModel) {
////		return SimpleVectorStore.builder(embeddingModel).build();
//		return AnalyticDbVectorStore.builder(embeddingModel).build();
//	}

}
