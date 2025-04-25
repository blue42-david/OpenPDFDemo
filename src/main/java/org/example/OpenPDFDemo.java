package org.example;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenPDFDemo {

    private static final String PDF_PATH = "demo.pdf";
    private static final String IMAGE_PATH = "sample-image.jpg"; // Provide your image path

    public static void main(String[] args) throws FileNotFoundException {
            // 1. Initialize document and writer
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_PATH));

            // Set encryption (owner password required for modifications)
            writer.setEncryption(
                    "userpass".getBytes(),
                    "ownerpass".getBytes(),
                    PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY,
                    PdfWriter.ENCRYPTION_AES_256_V3
            );

            document.open();

            // 2. Add formatted content
            addTitle(document);
            addParagraphs(document);
            addTable(document, createDummyData());
            addImage(document);

            document.close();
            System.out.println("PDF created successfully: " + PDF_PATH);

    }

    private static void addTitle(Document document) throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, new Color(0, 76, 153));
        Paragraph title = new Paragraph("OpenPDF Demo Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);
    }

    private static void addParagraphs(Document document) throws DocumentException {
        Font bodyFont = new Font(Font.TIMES_ROMAN, 12);
        Font highlightFont = new Font(Font.TIMES_ROMAN, 12, Font.ITALIC, Color.RED);

        document.add(new Paragraph("Generated on: " + new Date(), bodyFont));
        document.add(new Paragraph("This document demonstrates:", bodyFont));

        com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.ORDERED);
        list.add(new ListItem("Text formatting", bodyFont));
        list.add(new ListItem("Table creation", bodyFont));
        list.add(new ListItem("Image embedding", bodyFont));
        list.add(new ListItem("PDF encryption", highlightFont));
        document.add(list);

        document.add(Chunk.NEWLINE);
    }

    private static void addTable(Document document, List<User> users) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table headers
        String[] headers = {"First Name", "Last Name", "Email", "Date of Birth"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(new Color(220, 220, 220));
            table.addCell(cell);
        }

        // Table data
        for (User user : users) {
            table.addCell(user.firstName());
            table.addCell(user.lastName());
            table.addCell(user.email());
            table.addCell(user.birthdate().toString());
        }

        document.add(table);
    }

    private static void addImage(Document document) throws DocumentException {
        try {
            Image image = Image.getInstance(IMAGE_PATH);
            image.scaleToFit(400, 200);
            image.setAlignment(Image.ALIGN_CENTER);
            image.setBorder(Image.BOX);
            image.setBorderWidth(2);
            document.add(image);
        } catch (Exception e) {
            throw new DocumentException("Error loading image: " + e.getMessage());
        }
    }

    private static List<User> createDummyData() {
        List<User> users = new ArrayList<>();
        users.add(new User("John", "Doe", "john@example.com", new Date()));
        users.add(new User("Jane", "Smith", "jane@example.com", new Date()));
        users.add(new User("Bob", "Johnson", "bob@example.com", new Date()));
        return users;
    }

    record User (
         String firstName,
         String lastName,
         String email,
         Date birthdate
    ){}
}
