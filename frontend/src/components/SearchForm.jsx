function SearchForm({ estudianteId, onSetEstudianteId, onBuscarAcademico }) {
  return (
    <form className="search-form" onSubmit={onBuscarAcademico}>
      <input
        id="estudianteId"
        type="number"
        value={estudianteId}
        onChange={(event) => onSetEstudianteId(event.target.value)}
        placeholder="Ingresa ID de estudiante"
      />
      <button type="submit">🔍 Buscar</button>
    </form>
  );
}

export default SearchForm;
