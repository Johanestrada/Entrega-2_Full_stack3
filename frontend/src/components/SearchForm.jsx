function SearchForm({ query, onSetQuery, mode, onSetMode, onBuscarAcademico }) {
  return (
    <form className="search-form" onSubmit={onBuscarAcademico}>
      <div className="search-mode">
        <span className="search-mode-label">Buscar por</span>
        <div className="btn-group search-toggle" role="group" aria-label="Modo de búsqueda">
          <input
            type="radio"
            className="btn-check"
            name="searchMode"
            id="searchModeRun"
            value="run"
            checked={mode === 'run'}
            onChange={(event) => onSetMode(event.target.value)}
          />
          <label className={`btn btn-outline-primary ${mode === 'run' ? 'active' : ''}`} htmlFor="searchModeRun">
            RUN
          </label>

          <input
            type="radio"
            className="btn-check"
            name="searchMode"
            id="searchModeCurso"
            value="curso"
            checked={mode === 'curso'}
            onChange={(event) => onSetMode(event.target.value)}
          />
          <label className={`btn btn-outline-primary ${mode === 'curso' ? 'active' : ''}`} htmlFor="searchModeCurso">
            Curso
          </label>
        </div>
      </div>
      <input
        className="form-control form-control-lg"
        id="searchQuery"
        type="text"
        value={query}
        onChange={(event) => onSetQuery(event.target.value)}
        placeholder={mode === 'curso' ? 'Ingresa curso de estudiante' : 'Ingresa RUN de estudiante'}
      />
      <button type="submit" className="btn btn-primary btn-lg px-4 search-submit">
        Buscar
      </button>
    </form>
  );
}

export default SearchForm;
