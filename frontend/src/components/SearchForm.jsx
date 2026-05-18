function SearchForm({ query, onSetQuery, mode, onSetMode, onBuscarAcademico }) {
  return (
    <form className="search-form" onSubmit={onBuscarAcademico}>
      <div className="search-mode">
        <label>
          <input
            type="radio"
            name="searchMode"
            value="run"
            checked={mode === 'run'}
            onChange={(event) => onSetMode(event.target.value)}
          />
          Buscar por RUN
        </label>
        <label>
          <input
            type="radio"
            name="searchMode"
            value="curso"
            checked={mode === 'curso'}
            onChange={(event) => onSetMode(event.target.value)}
          />
          Buscar por curso
        </label>
      </div>
      <input
        id="searchQuery"
        type="text"
        value={query}
        onChange={(event) => onSetQuery(event.target.value)}
        placeholder={mode === 'curso' ? 'Ingresa curso de estudiante' : 'Ingresa RUN de estudiante'}
      />
      <button type="submit">🔍 Buscar</button>
    </form>
  );
}

export default SearchForm;
