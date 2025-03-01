package br.com.evento.rest.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@FacesConverter(forClass = LocalDate.class, value = "dataConverter")
public class DataConverter implements Converter<LocalDate> {

    private final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate getAsObject(FacesContext facesContext, UIComponent uiComponent, String valor) {
        if (valor != null && !valor.isEmpty())
            return LocalDate.parse(valor, formatador);

        return null;
    }


    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, LocalDate data) {
        if (data != null)
            return data.format(formatador);

        return "";
    }
}
