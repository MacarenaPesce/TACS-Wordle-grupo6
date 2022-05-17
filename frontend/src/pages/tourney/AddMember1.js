import {useAddMember} from '../../hooks/useAddMember';
import FormAddMember from './FormAddMember';
import ModalAddMember from './ModalAddMember';
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";


function AddMember1 (tourneyId)  {

    const [isOpenModal,openModal,closeModal] = useAddMember(false);
console.log("voy a mostrar eltourneyId: ", tourneyId);

return(
	<div>	
		<button type="submit" className="btn btn-warning" onClick={openModal}> <AiOutlineUsergroupAdd/> </button>
		<ModalAddMember isOpen={isOpenModal} closeModal={closeModal}>
			<FormAddMember/>
		</ModalAddMember>
	</div>

	);
}
export default AddMember1;