using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.Models
{
    public class Media
    {
        public Media()
        {
        }

        public Media(string mimeType)
        {
            switch (mimeType)
            {
                case "image/jpeg": Type = MediaType.Photo; break;
                case "video/mp4": Type = MediaType.Video; break;
            }
        }

        public int MediaID { get; set; }
        public string Description { get; set; }
        public MediaType Type { get; set; }

        public DateTime UploadedOn { get; set; }

        public int UserID { get; set; }
        public virtual User Owner { get; set; }
        public int TownID { get; set; }
        public virtual Town Town { get; set; }

        public string Path
        {
            get
            {
                string extension = "";
                switch (this.Type)
                {
                    case MediaType.Photo: extension = "jpg"; break;
                    case MediaType.Video: extension = "mp4"; break;
                }

                return String.Format("{0}\\{1}.{2}", TownID, MediaID, extension);
            }

        }
    }
}
