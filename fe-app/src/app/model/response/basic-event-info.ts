import { ImageResponse } from '../image/img-response.model';
import { UserResponse } from '../user/user-response,model';

export interface BasicEventInfo {
  id: number;
  image: ImageResponse;
  startDate: Date;
  address: string;
  name: string;
  shortDescription: string;
  organizer: UserResponse;
}
