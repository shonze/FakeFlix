import React from 'react';
import './Field.css';

const Field = ({ label, name, value, onChange, type, required}) => {
    return (
        <div className="form-column">
            <label className="form-label">{label}</label>
            <input
                type={type}
                name={name}
                className="custom-input"
                value={value}
                onChange={onChange}
                required={required}
            />
        </div>
    );
};

export default Field;
