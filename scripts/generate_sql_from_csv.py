#!/usr/bin/env python3
import csv
import sys

def generate_sql_from_csv(csv_file_path, output_file_path):
    """
    Convierte el archivo CSV a un script SQL para insertar en la tabla short_urls
    """
    
    sql_content = """-- Script para poblar la tabla short_urls con datos de prueba para JMeter
-- Generado automáticamente desde test_urls.csv

-- Limpiar datos existentes (opcional)
-- TRUNCATE TABLE short_urls CASCADE;

-- Insertar datos de prueba
INSERT INTO short_urls (id, url, short_code, created_at, updated_at, access_count) VALUES
"""
    
    values = []
    
    with open(csv_file_path, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        for row in reader:
            # Escapar comillas simples en las URLs
            url = row['url'].replace("'", "''")
            short_code = row['short_code'].replace("'", "''")
            
            value = f"({row['id']}, '{url}', '{short_code}', '{row['created_at']}', '{row['update_at']}', {row['access_count']})"
            values.append(value)
    
    sql_content += ',\n'.join(values) + ';\n'
    
    # Agregar configuración de secuencia para el ID
    sql_content += f"\n-- Actualizar la secuencia para evitar conflictos de ID\nSELECT setval('short_urls_id_seq', {len(values)}, true);\n"
    
    with open(output_file_path, 'w', encoding='utf-8') as output_file:
        output_file.write(sql_content)
    
    print(f"Script SQL generado exitosamente: {output_file_path}")

if __name__ == "__main__":
    csv_path = "backend/jmeter/testdata/test_urls.csv"
    sql_path = "backend/db/populate_test_data.sql"
    generate_sql_from_csv(csv_path, sql_path)