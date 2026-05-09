package com.kunal.autoops.intel;

import org.springframework.stereotype.Service;

@Service
public class IncidentEmbeddingService {
    // Placeholder embedding service - integrate external vector DB or pgvector later
    public String embed(String text) {
        if (text == null) return "";
        // naive placeholder: return first 200 chars as "embedding"
        return text.length() <= 200 ? text : text.substring(0, 200);
    }
}
