import { useEffect, useState, useCallback } from "react";
import { FaShieldAlt, FaUserShield, FaSignOutAlt, FaPlus, FaFileContract, FaMoneyBillWave, FaCheckCircle, FaExclamationCircle } from "react-icons/fa";
import 'bootstrap/dist/css/bootstrap.min.css';

// --- COMPONENTE 1: PANTALLA DE LOGIN ---
const LoginScreen = ({ onLogin, error }) => {
  const [localCreds, setLocalCreds] = useState({ username: "", password: "" });

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin(localCreds);
  };

  const handleChange = (e) => setLocalCreds({ ...localCreds, [e.target.name]: e.target.value });

  return (
    <div className="d-flex align-items-center justify-content-center vh-100" style={{ background: "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)" }}>
      <div className="card shadow-lg border-0" style={{ width: "400px", borderRadius: "15px" }}>
        <div className="card-body p-5">
          <div className="text-center mb-4">
            <div className="bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{ width: "60px", height: "60px" }}>
              <FaShieldAlt size={30} />
            </div>
            <h3 className="fw-bold text-dark">OpenPolicy</h3>
            <p className="text-muted small">Acceso Seguro al Sistema</p>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-floating mb-3">
              <input 
                type="text" name="username" className="form-control" id="floatingInput" 
                placeholder="Usuario" onChange={handleChange} 
              />
              <label htmlFor="floatingInput">Usuario</label>
            </div>
            <div className="form-floating mb-3">
              <input 
                type="password" name="password" className="form-control" id="floatingPassword" 
                placeholder="Contraseña" onChange={handleChange} 
              />
              <label htmlFor="floatingPassword">Contraseña</label>
            </div>

            {error && (
              <div className="alert alert-danger d-flex align-items-center small py-2" role="alert">
                <FaExclamationCircle className="me-2" /> Credenciales incorrectas
              </div>
            )}

            <button className="btn btn-primary w-100 py-2 fw-bold shadow-sm" style={{ borderRadius: "8px" }}>
              INGRESAR
            </button>
          </form>
          <div className="text-center mt-4">
            <span className="text-muted small">Powered by Spring Boot & React</span>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- COMPONENTE 2: TARJETAS DE ESTADÍSTICAS (KPIs) ---
const StatsCards = ({ policies }) => {
  const total = policies.length;
  const active = policies.filter(p => p.status === 'ACTIVE').length;
  const pending = policies.filter(p => p.status === 'PENDING').length;

  return (
    <div className="row mb-4">
      <div className="col-md-4">
        <div className="card border-0 shadow-sm text-white bg-primary h-100">
          <div className="card-body d-flex align-items-center">
            <FaFileContract size={40} className="opacity-50 me-3" />
            <div>
              <h6 className="text-uppercase mb-0">Total Pólizas</h6>
              <h2 className="fw-bold mb-0">{total}</h2>
            </div>
          </div>
        </div>
      </div>
      <div className="col-md-4">
        <div className="card border-0 shadow-sm text-white bg-success h-100">
          <div className="card-body d-flex align-items-center">
            <FaCheckCircle size={40} className="opacity-50 me-3" />
            <div>
              <h6 className="text-uppercase mb-0">Activas</h6>
              <h2 className="fw-bold mb-0">{active}</h2>
            </div>
          </div>
        </div>
      </div>
      <div className="col-md-4">
        <div className="card border-0 shadow-sm text-dark bg-warning h-100">
          <div className="card-body d-flex align-items-center">
            <FaExclamationCircle size={40} className="opacity-50 me-3" />
            <div>
              <h6 className="text-uppercase mb-0">Pendientes</h6>
              <h2 className="fw-bold mb-0">{pending}</h2>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- COMPONENTE PRINCIPAL ---
function App() {
  // LÓGICA (Estados)
  const [token, setToken] = useState(localStorage.getItem("jwt_token") || ""); 
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("jwt_token"));
  const [loginError, setLoginError] = useState(false);
  const [policies, setPolicies] = useState([]);
  const [formData, setFormData] = useState({ policyNumber: "", holderName: "", premiumAmount: "", startDate: "" });
  const [formErrors, setFormErrors] = useState({});

  // UTILIDADES
  const logout = useCallback(() => {
    setToken("");
    localStorage.removeItem("jwt_token");
    setIsLoggedIn(false);
    setPolicies([]);
  }, []);

  const fetchPolicies = useCallback(() => {
    fetch("http://localhost:8081/api/policies", {
      headers: { "Authorization": `Bearer ${token}` }
    })
      .then((res) => {
        if (res.status === 403 || res.status === 401) { logout(); return; }
        return res.json();
      })
      .then((data) => { if(data && Array.isArray(data)) setPolicies(data); })
      .catch(err => console.error(err));
  }, [token, logout]);

  useEffect(() => {
    if (isLoggedIn && token) fetchPolicies();
  }, [isLoggedIn, token, fetchPolicies]);

  // MANEJADORES
  const handleLogin = (creds) => {
    setLoginError(false);
    fetch("http://localhost:8081/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(creds)
    })
    .then(res => { if (res.ok) return res.json(); throw new Error("Error"); })
    .then(data => {
      setToken(data.token);
      localStorage.setItem("jwt_token", data.token);
      setIsLoggedIn(true);
    })
    .catch(() => setLoginError(true));
  };

  const handleCreate = (e) => {
    e.preventDefault();
    setFormErrors({});
    fetch("http://localhost:8081/api/policies", {
      method: "POST",
      headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
      body: JSON.stringify(formData),
    })
      .then(async (res) => {
        if (res.ok) return res.json();
        const err = await res.json(); throw err; 
      })
      .then(() => {
        fetchPolicies();
        setFormData({ policyNumber: "", holderName: "", premiumAmount: "", startDate: "" });
      })
      .catch((err) => {
        if(err.policyNumber || err.holderName) setFormErrors(err);
        else alert("Error: " + (err.message || "Desconocido"));
      });
  };

  // --- RENDERIZADO CONDICIONAL ---
  if (!isLoggedIn) {
    return <LoginScreen onLogin={handleLogin} error={loginError} />;
  }

  // --- DASHBOARD UI ---
  return (
    <div className="vh-100 d-flex flex-column" style={{ backgroundColor: "#f0f2f5" }}>
      {/* Navbar */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm px-4">
        <div className="container-fluid">
          <span className="navbar-brand fw-bold d-flex align-items-center">
            <FaShieldAlt className="me-2" /> OpenPolicy <span className="badge bg-secondary ms-2 text-uppercase" style={{fontSize: "0.6em"}}>Enterprise</span>
          </span>
          <div className="d-flex align-items-center text-white">
            <FaUserShield className="me-2" /> Admin User
            <button onClick={logout} className="btn btn-outline-light btn-sm ms-3 d-flex align-items-center">
              <FaSignOutAlt className="me-1"/> Salir
            </button>
          </div>
        </div>
      </nav>

      {/* Contenido */}
      <div className="container mt-4 flex-grow-1">
        
        {/* KPI Cards */}
        <StatsCards policies={policies} />

        <div className="row">
          {/* Formulario (Columna Izquierda) */}
          <div className="col-lg-4 mb-4">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-header bg-white border-0 fw-bold py-3 text-primary d-flex align-items-center">
                <FaPlus className="me-2"/> Nueva Póliza
              </div>
              <div className="card-body">
                <form onSubmit={handleCreate} className="d-flex flex-column gap-3">
                  <div>
                    <label className="form-label small fw-bold text-muted">Número de Póliza</label>
                    <input type="text" className={`form-control ${formErrors.policyNumber ? 'is-invalid' : ''}`} 
                      placeholder="Ej. POL-1234" 
                      value={formData.policyNumber} 
                      onChange={e => setFormData({...formData, policyNumber: e.target.value})} />
                    <div className="invalid-feedback">{formErrors.policyNumber}</div>
                  </div>
                  
                  <div>
                    <label className="form-label small fw-bold text-muted">Titular</label>
                    <input type="text" className={`form-control ${formErrors.holderName ? 'is-invalid' : ''}`} 
                      placeholder="Nombre Completo" 
                      value={formData.holderName} 
                      onChange={e => setFormData({...formData, holderName: e.target.value})} />
                    <div className="invalid-feedback">{formErrors.holderName}</div>
                  </div>

                  <div className="row">
                    <div className="col-6">
                      <label className="form-label small fw-bold text-muted">Prima</label>
                      <div className="input-group">
                        <span className="input-group-text">$</span>
                        <input type="number" className={`form-control ${formErrors.premiumAmount ? 'is-invalid' : ''}`} 
                          placeholder="0.00" 
                          value={formData.premiumAmount} 
                          onChange={e => setFormData({...formData, premiumAmount: e.target.value})} />
                      </div>
                      <div className="text-danger small mt-1">{formErrors.premiumAmount}</div>
                    </div>
                    <div className="col-6">
                      <label className="form-label small fw-bold text-muted">Inicio</label>
                      <input type="date" className={`form-control ${formErrors.startDate ? 'is-invalid' : ''}`} 
                        value={formData.startDate} 
                        onChange={e => setFormData({...formData, startDate: e.target.value})} />
                      <div className="text-danger small mt-1">{formErrors.startDate}</div>
                    </div>
                  </div>

                  <button className="btn btn-primary w-100 fw-bold mt-2 shadow-sm">
                    Guardar Póliza
                  </button>
                </form>
              </div>
            </div>
          </div>

          {/* Tabla (Columna Derecha) */}
          <div className="col-lg-8">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-header bg-white border-0 fw-bold py-3 text-dark d-flex justify-content-between align-items-center">
                <span><FaFileContract className="me-2"/> Pólizas Recientes</span>
                <span className="badge bg-light text-dark border">{policies.length} Registros</span>
              </div>
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead className="table-light text-muted small text-uppercase">
                    <tr>
                      <th className="ps-4">Número</th>
                      <th>Titular</th>
                      <th>Prima</th>
                      <th>Inicio</th>
                      <th>Estado</th>
                    </tr>
                  </thead>
                  <tbody className="border-top-0">
                    {policies.length === 0 ? (
                      <tr>
                        <td colSpan="5" className="text-center py-5 text-muted">
                          No hay pólizas registradas.
                        </td>
                      </tr>
                    ) : (
                      policies.map(p => (
                        <tr key={p.id}>
                          <td className="ps-4 fw-bold text-primary">{p.policyNumber}</td>
                          <td>
                            <div className="d-flex align-items-center">
                              <div className="bg-light rounded-circle d-flex align-items-center justify-content-center me-2" style={{width: '32px', height: '32px'}}>
                                <span className="small fw-bold text-secondary">{p.holderName.charAt(0)}</span>
                              </div>
                              {p.holderName}
                            </div>
                          </td>
                          <td className="fw-bold"><FaMoneyBillWave className="text-success me-1"/>{p.premiumAmount}</td>
                          <td className="text-muted small">{p.startDate}</td>
                          <td>
                            <span className={`badge rounded-pill ${p.status === 'ACTIVE' ? 'bg-success' : 'bg-warning text-dark'}`}>
                              {p.status}
                            </span>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;