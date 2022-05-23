import {useAddMember} from '../../hooks/useAddMember';
import FormAddMember from './FormAddMember';
import ModalAddMember from './ModalAddMember';
import { AiOutlineUsergroupAdd } from "react-icons/ai";
import React from 'react'


function AddMember (tourneyId)  {

    const [isOpenModal,openModal,closeModal] = useAddMember(false);

	return(
		<React.Fragment>
			<button type="submit" className="btn btn-warning" onClick={openModal}> <AiOutlineUsergroupAdd/> </button>
			<ModalAddMember isOpen={isOpenModal} closeModal={closeModal}>
				<FormAddMember/>
			</ModalAddMember>
		</React.Fragment>
	);
}
export default AddMember;