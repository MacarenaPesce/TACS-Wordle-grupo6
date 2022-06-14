import './ModalAddMember.css';

const ModalAddMember =({children, isOpen, closeModal}) => {
	const handleModalContainerClick = (e) => e.stopPropagation();

	return (
		<div className={  `modal ${isOpen && 'is-open'}`} onClick={closeModal}>
			<div className="modal-container" onClick={handleModalContainerClick}>
				{children}
				<button className="modal-close" onClick={closeModal}>
					Agregar
				</button>
			</div>
		</div>
	);
};

export default ModalAddMember;